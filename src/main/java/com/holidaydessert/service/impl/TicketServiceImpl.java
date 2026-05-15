package com.holidaydessert.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.constant.TicketConstant;
import com.holidaydessert.kafka.TicketOrderProducer;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.model.TicketType;
import com.holidaydessert.repository.TicketTypeRepository;
import com.holidaydessert.service.TicketOrderService;
import com.holidaydessert.service.TicketService;
import com.holidaydessert.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;  // ← StringRedisTemplate（純字串，Lua 相容）
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {

    // 使用 StringRedisTemplate：所有值以純字串取，不會有 Jackson 引號問題
    // Lua tonumber("100") ✓  vs  Jackson 序列化 tonumber("\"100\"") ✗
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketOrderService ticketOrderService;
    
    @Autowired(required = false)
    private TicketOrderProducer ticketOrderProducer;
    
    // ─── JUC 本地防護 ────────────────────────────────────────────
    /** Layer 1：Semaphore 流控（限制同時進入 Redis 的併發數） */
    private final Semaphore SEMAPHORE = new Semaphore(200);

    /**
     * Layer 2：本地剩餘計數器（typeId → AtomicInteger）
     * 售罄後直接本地攔截，不打 Redis
     */
    private final ConcurrentHashMap<Integer, AtomicInteger> LOCAL_REMAINING = new ConcurrentHashMap<>();

    // ─────────────────────────────────────────────────────────────
    // 【售票前】初始化：DB → Redis 三層結構
    // ─────────────────────────────────────────────────────────────
    @Override
    public String initTickets(String ticketName) {
        String lockKey   = String.format(TicketConstant.INIT_LOCK_KEY, ticketName);
        String lockValue = UUID.randomUUID().toString();

        if (!redisLockUtil.lock(lockKey, lockValue, 30)) {
            return "初始化進行中，請勿重複操作";
        }

        try {
            // 1. DB 查詢（JOIN FETCH 一次取得活動 + 所有票種）
            List<TicketType> types = ticketTypeRepository.findByTicketName(ticketName);
            if (types.isEmpty()) {
                return "找不到活動「" + ticketName + "」的票種，請確認名稱";
            }

            for (TicketType type : types) {
                Integer typeId = type.getTypeId();
                Ticket  ticket = type.getTicket(); // JOIN FETCH，不另外查 DB

                // ─── 第一層：票種靜態資訊（Hash，幾乎不變） ──────────
                String infoKey = String.format(TicketConstant.TICKET_TYPE_INFO_KEY, typeId);
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put(TicketConstant.FIELD_TYPE_NAME,            type.getTypeName());
                infoMap.put(TicketConstant.FIELD_TYPE_PRICE,           String.valueOf(type.getTypePrice()));
                infoMap.put(TicketConstant.FIELD_TYPE_MAX_PER_PERSON,  String.valueOf(type.getTypeMaxPerPerson()));
                infoMap.put(TicketConstant.FIELD_TICKET_NAME,          ticket.getTicketName());
                infoMap.put(TicketConstant.FIELD_TICKET_STATUS,        String.valueOf(ticket.getTicketStatus()));
                infoMap.put(TicketConstant.FIELD_SALE_START,           ticket.getTicketSaleStart().toString());
                infoMap.put(TicketConstant.FIELD_SALE_END,             ticket.getTicketSaleEnd().toString());
                stringRedisTemplate.opsForHash().putAll(infoKey, infoMap);
                stringRedisTemplate.expire(infoKey, TicketConstant.REDIS_TTL_HOURS, TimeUnit.HOURS);

                // ─── 第二層：剩餘數（String，高頻原子操作） ─────────
                String remainingKey = String.format(TicketConstant.TICKET_REMAINING_KEY, typeId);
                Boolean isNew = stringRedisTemplate.opsForValue().setIfAbsent(remainingKey, String.valueOf(type.getTypeRemaining())); // ✅ 已存在則跳過

                if (Boolean.TRUE.equals(isNew)) {
                    stringRedisTemplate.expire(remainingKey, TicketConstant.REDIS_TTL_HOURS, TimeUnit.HOURS);
                    LOCAL_REMAINING.put(typeId, new AtomicInteger(type.getTypeRemaining()));
                    log.info("[Init] ✅ 新建 typeId={} remaining={}", typeId, type.getTypeRemaining());
                } else {
                    // 從 Redis 同步本地計數器（避免 LOCAL_REMAINING 為 0 而 Redis 還有票）
                    String currentRemaining = stringRedisTemplate.opsForValue().get(remainingKey);
                    int current = currentRemaining != null ? Integer.parseInt(currentRemaining) : 0;
                    LOCAL_REMAINING.put(typeId, new AtomicInteger(current));
                    log.info("[Init] ⏭️ 已存在，跳過覆蓋 typeId={} current={}", typeId, current);
                }
                // ─── 第三層：ticket:user:count 不預熱 ────────────────
                //     使用者購票時才由 Lua INCRBY 建立，不存在即為 0
                
                
                
                log.info("[Init] ✅ 票種預熱完成 typeId={} typeName={} remaining={}", typeId, type.getTypeName(), type.getTypeRemaining());
            }

            return String.format("活動「%s」預熱完成，共 %d 種票種", ticketName, types.size());

        } finally {
            redisLockUtil.unlock(lockKey, lockValue);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 【搶票中】購票：三層防護 + 異步 DB 持久化
    // ────────────────────────────────────────────────────────────
    @Override
    public String purchaseTicket(Integer typeId, Integer memId, Integer quantity) {

        // ── Layer 1：Semaphore 流控 ─────────────────────────────────
        if (!SEMAPHORE.tryAcquire()) {
            log.warn("[Layer1-Reject] 系統繁忙 typeId={} memId={}", typeId, memId);
            return "系統繁忙請稍後再試";
        }

        try {
            // ── Layer 2：本地計數器快速失敗 ──────────────────────────
            AtomicInteger localRemaining = LOCAL_REMAINING.get(typeId);
            if (localRemaining == null) {
                return "票種尚未初始化，請聯繫管理員";
            }
            if (localRemaining.get() < quantity) {
                log.info("[Layer2-FastFail] 本地判斷票不足 typeId={} memId={} local={}", typeId, memId, localRemaining.get());
                return "票已售罄";
            }

            // 從 Redis Hash 讀取靜態資訊（每人上限）
            String infoKey = String.format(TicketConstant.TICKET_TYPE_INFO_KEY, typeId);
            String maxPerPersonStr = (String) stringRedisTemplate.opsForHash().get(infoKey, TicketConstant.FIELD_TYPE_MAX_PER_PERSON);
            if (maxPerPersonStr == null) {
                return "票種資訊已過期，請聯繫管理員重新初始化";
            }
            int maxPerPerson = Integer.parseInt(maxPerPersonStr);

            // ── Layer 3：Redis Lua 原子操作（扣票 + 個人上限）───────────
            String remainingKey  = String.format(TicketConstant.TICKET_REMAINING_KEY, typeId);
            String userCountKey  = String.format(TicketConstant.TICKET_USER_COUNT_KEY, typeId, memId);

            Long result = stringRedisTemplate.execute(
            	    TicketConstant.PURCHASE_SCRIPT,
            	    Arrays.asList(remainingKey, userCountKey),
            	    String.valueOf(quantity),
            	    String.valueOf(maxPerPerson),
            	    String.valueOf(TicketConstant.USER_COUNT_TTL_SECONDS)
            );

            if (result == null || result == -1L) {
                localRemaining.set(0);
                log.info("[Layer3-SoldOut] Redis 確認售罄 typeId={} memId={}", typeId, memId);
                return "票已售罄";
            }
            if (result == -2L) {
                log.info("[Layer3-LimitExceed] 超過個人上限 typeId={} memId={} limit={}", typeId, memId, maxPerPerson);
                return "已超過個人購買上限（上限 " + maxPerPerson + " 張）";
            }

            // 同步本地計數器：只允許往小的方向更新，防止過時值污染
            localRemaining.updateAndGet(current -> Math.min(current, result.intValue()));

            // 取得票價（用於訂單金額快照）
            String priceStr = (String) stringRedisTemplate.opsForHash().get(infoKey, TicketConstant.FIELD_TYPE_PRICE);
            int price = priceStr != null ? Integer.parseInt(priceStr) : 0;

            // ── Layer 4：異步 DB 持久化（不阻塞主流程）──────────────────
            if (ticketOrderProducer != null) {
                ticketOrderProducer.sendOrder(typeId, memId, quantity, price); // ✅ Kafka 模式
            } else {
                ticketOrderService.persistOrder(typeId, memId, quantity, price); // ✅ @Async 降級
            }

            log.info("[Success] 搶票成功 typeId={} memId={} quantity={} remaining={}", typeId, memId, quantity, result);
            return String.format("搶票成功！購買 %d 張，剩餘票數：%d", quantity, result);

        } finally {
            SEMAPHORE.release();
        }
    }

    @Override
    public int remainCount(Integer typeId) {
        String remainingKey = String.format(TicketConstant.TICKET_REMAINING_KEY, typeId);
        String count = stringRedisTemplate.opsForValue().get(remainingKey);
        return count != null ? Integer.parseInt(count) : 0;
    }
    
    // ────────────────── 管理用途維持 RedisTemplate<String, Object>（存放 Ticket 物件）──────────────────
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void saveToRedis(Ticket ticket) {
        String key = String.format(TicketConstant.TICKET_ID_KEY, ticket.getTicketId());
        redisTemplate.opsForValue().set(key, ticket);
    }

    @Override
    public Ticket getFromRedis(Long id) {
        String key = String.format(TicketConstant.TICKET_ID_KEY, id);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) return null;
        try {
            return objectMapper.convertValue(obj, Ticket.class);
        } catch (Exception e) {
            log.error("[Redis] Ticket 轉換失敗 id={}", id, e);
            return null;
        }
    }

    @Override
    public void deleteFromRedis(Long id) {
        String key = String.format(TicketConstant.TICKET_ID_KEY, id);
        redisTemplate.delete(key);
    }
    
}

package com.holidaydessert.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.constant.TicketConstant;
import com.holidaydessert.constant.TicketVenue;
import com.holidaydessert.kafka.TicketOrderProducer;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.model.TicketType;
import com.holidaydessert.repository.TicketOrderRepository;
import com.holidaydessert.repository.TicketRepository;
import com.holidaydessert.repository.TicketTypeRepository;
import com.holidaydessert.utils.PageableUtil;
import com.holidaydessert.utils.RedisLockUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketService {

    // ─── JUC 本地防護 ────────────────────────────────────────────
    // Layer 1：Semaphore 流控（限制同時進入 Redis 的併發數）
    private final Semaphore SEMAPHORE = new Semaphore(200);
    // Layer 2：本地剩餘計數器（typeId → AtomicInteger）。售罄後直接本地攔截，不打 Redis
    private final ConcurrentHashMap<Integer, AtomicInteger> LOCAL_REMAINING = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketOrderService ticketOrderService;

    @Autowired
    private TicketTypeService ticketTypeService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketOrderRepository ticketOrderRepository;
    
    @Autowired(required = false)
    private TicketOrderProducer ticketOrderProducer;

    // ─────────────────────────────────────────────────────────────
    // 【售票前】初始化：DB → Redis 三層結構
    // ─────────────────────────────────────────────────────────────
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
                    buildSeatPool(type);
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

    // ─── 座位池：ticket:seats:{typeId}（List，前排優先、同排連號）────────
    //     只在剩餘數 Key 新建時重建，避免覆蓋搶票中的座位池
    private void buildSeatPool(TicketType type) {
        String seatsKey = String.format(TicketConstant.TICKET_SEATS_KEY, type.getTypeId());
        stringRedisTemplate.delete(seatsKey);
        if (!type.hasSeatMap()) {
            return; // 自由座不配位
        }
        List<String> seats = ticketTypeService.availableSeats(type);
        if (!seats.isEmpty()) {
            stringRedisTemplate.opsForList().rightPushAll(seatsKey, seats);
            stringRedisTemplate.expire(seatsKey, TicketConstant.REDIS_TTL_HOURS, TimeUnit.HOURS);
        }
        log.info("[Init] 🪑 座位池建立 typeId={} seats={}", type.getTypeId(), seats.size());
    }
    

    // ─────────────────────────────────────────────────────────────
    // 【搶票中】購票：三層防護 + 異步 DB 持久化
    // ────────────────────────────────────────────────────────────
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
                return "此票種尚未開放或已停止售票";
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
            String seatsKey      = String.format(TicketConstant.TICKET_SEATS_KEY, typeId);

            List<?> scriptResult = stringRedisTemplate.execute(
            	    TicketConstant.PURCHASE_SCRIPT,
            	    Arrays.asList(remainingKey, userCountKey, seatsKey),
            	    String.valueOf(quantity),
            	    String.valueOf(maxPerPerson),
            	    String.valueOf(TicketConstant.USER_COUNT_TTL_SECONDS)
            );
            // 第一個元素為剩餘票數（或錯誤碼），其後為配到的座位
            Long result = scriptResult == null || scriptResult.isEmpty() ? null : Long.valueOf(String.valueOf(scriptResult.get(0)));
            String seats = scriptResult != null && scriptResult.size() > 1
                    ? String.join(",", scriptResult.subList(1, scriptResult.size()).stream().map(String::valueOf).toList())
                    : null;

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
                ticketOrderProducer.sendOrder(typeId, memId, quantity, price, seats); // ✅ Kafka 模式
            } else {
                ticketOrderService.persistOrder(typeId, memId, quantity, price, seats); // ✅ @Async 降級
            }

            log.info("[Success] 搶票成功 typeId={} memId={} quantity={} remaining={} seats={}", typeId, memId, quantity, result, seats);
            String message = String.format("搶票成功！購買 %d 張，剩餘票數：%d", quantity, result);
            return seats != null ? message + "，座位：" + TicketTypeService.formatSeats(seats) : message;

        } finally {
            SEMAPHORE.release();
        }
    }
    
    /** 查詢指定票種剩餘票數 */
    public int remainCount(Integer typeId) {
        String remainingKey = String.format(TicketConstant.TICKET_REMAINING_KEY, typeId);
        String count = stringRedisTemplate.opsForValue().get(remainingKey);
        return count != null ? Integer.parseInt(count) : 0;
    }
    
    // =============================================
    // back：後台活動管理
    // =============================================
    public Map<String, Object> list(Map<String, String> dataTableParams) {
        String keyword = PageableUtil.getStringParam(dataTableParams, "search[value]", "").trim();
        String draw = PageableUtil.getStringParam(dataTableParams, "draw", "0");
        Pageable pageable = PageableUtil.buildDataTablePageable(
                dataTableParams,
                SortFieldsConstant.TICKET_TABLE,
                SortFieldsConstant.TICKET_DEFAULT,
                Sort.Direction.DESC
        );
        Page<Map<String, Object>> ticketPage = ticketRepository.backList(keyword, pageable);
        List<Map<String, Object>> ticketList = ticketPage.getContent().stream()
                .map(row -> {
                    Map<String, Object> map = new LinkedHashMap<>(row);
                    map.put("VENUE_NAME", TicketVenue.of(String.valueOf(row.get("TICKET_VENUE"))).getVenueName());
                    Integer ticketId = ((Number) row.get("TICKET_ID")).intValue();
                    map.put("WARMED_TYPES", warmedTypeCount(ticketId));
                    return map;
                })
                .toList();
        return PageableUtil.buildDataTable(ticketPage, ticketList, draw);
    }

    public Ticket getData(Integer ticketId) {
        return ticketId == null ? null : ticketRepository.findById(ticketId).orElse(null);
    }

    /** 新增活動，並依場地模板產生分區與票數；回傳錯誤訊息，成功回傳 null */
    @Transactional
    public String add(Ticket ticket) {
        String error = validate(ticket);
        if (error != null) return error;
        if (ticketRepository.existsByTicketName(ticket.getTicketName())) return "活動名稱已存在";

        TicketVenue venue = TicketVenue.of(ticket.getTicketVenue());
        ticket.setTicketId(null);
        ticket.setTicketVenue(venue.name());
        ticket.setTicketQuantity(venue.getCapacity());
        Ticket saved = ticketRepository.save(ticket);
        ticketTypeService.saveAll(ticketTypeService.buildZones(saved, venue));
        log.info("[Admin] 新增活動 ticketId={} venue={} zones={} quantity={}", saved.getTicketId(), venue, venue.getZones().size(), venue.getCapacity());
        return null;
    }

    /** 修改活動基本資料（場地、票數不在此修改，請至座位配置頁）；回傳錯誤訊息，成功回傳 null */
    public String update(Ticket form) {
        Ticket ticket = getData(form.getTicketId());
        if (ticket == null) return "查無此活動";
        String error = validate(form);
        if (error != null) return error;
        if (ticketRepository.existsByTicketNameAndTicketIdNot(form.getTicketName(), form.getTicketId())) return "活動名稱已存在";

        ticket.setTicketName(form.getTicketName());
        ticket.setTicketStart(form.getTicketStart());
        ticket.setTicketSaleStart(form.getTicketSaleStart());
        ticket.setTicketSaleEnd(form.getTicketSaleEnd());
        ticket.setTicketStatus(form.getTicketStatus());
        ticketRepository.save(ticket);
        return null;
    }

    private String validate(Ticket ticket) {
        if (ticket.getTicketName() == null || ticket.getTicketName().isBlank()) return "請輸入活動名稱";
        if (ticket.getTicketStart() == null) return "請選擇活動時間";
        if (ticket.getTicketSaleStart() == null || ticket.getTicketSaleEnd() == null) return "請選擇售票期間";
        if (ticket.getTicketSaleStart().isAfter(ticket.getTicketSaleEnd())) return "售票開始時間不得大於結束時間";
        if (ticket.getTicketStatus() == null) ticket.setTicketStatus(0);
        return null;
    }

    /** 依分區重新加總活動總票數 */
    public void refreshQuantity(Integer ticketId) {
        ticketRepository.refreshQuantity(ticketId);
    }

    /**
     * 清除單一票種的 Redis 快取（資訊、剩餘數、座位池、個人購買數）與本地計數器
     * 分區修改後呼叫，下次預熱會依 DB 重建
     */
    public void evictType(Integer typeId) {
        stringRedisTemplate.delete(Arrays.asList(
                String.format(TicketConstant.TICKET_TYPE_INFO_KEY, typeId),
                String.format(TicketConstant.TICKET_REMAINING_KEY, typeId),
                String.format(TicketConstant.TICKET_SEATS_KEY, typeId)));

        String userCountPattern = String.format(TicketConstant.TICKET_USER_COUNT_PATTERN, typeId);
        List<String> userCountKeys = new ArrayList<>();
        try (Cursor<String> cursor = stringRedisTemplate.scan(ScanOptions.scanOptions().match(userCountPattern).count(1000).build())) {
            cursor.forEachRemaining(userCountKeys::add);
        }
        if (!userCountKeys.isEmpty()) {
            stringRedisTemplate.delete(userCountKeys);
        }
        LOCAL_REMAINING.remove(typeId);
    }

    // =============================================
    // 預熱狀態：開啟 / 關閉
    // =============================================

    /** 已預熱（Redis 有剩餘數 Key）的票種數 */
    public int warmedTypeCount(Integer ticketId) {
        return (int) ticketTypeRepository.findByTicketId(ticketId).stream()
                .filter(type -> Boolean.TRUE.equals(stringRedisTemplate.hasKey(String.format(TicketConstant.TICKET_REMAINING_KEY, type.getTypeId()))))
                .count();
    }

    /** Redis 中所有已預熱票種的剩餘票數加總；未預熱回傳 -1 */
    public int warmedRemaining(Integer ticketId) {
        int total = -1;
        for (TicketType type : ticketTypeRepository.findByTicketId(ticketId)) {
            String remaining = stringRedisTemplate.opsForValue().get(String.format(TicketConstant.TICKET_REMAINING_KEY, type.getTypeId()));
            if (remaining != null) {
                total = Math.max(total, 0) + Integer.parseInt(remaining);
            }
        }
        return total;
    }

    /**
     * 關閉預熱：清除 Redis 的票種資訊、剩餘數、座位池，停止搶票
     * DB 剩餘票數已由非同步寫入同步，不需回寫；個人已購數量保留到 TTL 到期，重新預熱後上限仍有效
     * 已售罄的票種保留本地計數器 0，之後的請求仍回覆「票已售罄」而不是「停止售票」
     */
    public String closeWarmUp(Integer ticketId, String reason) {
        Ticket ticket = getData(ticketId);
        if (ticket == null) return "查無此活動";

        int closed = 0;
        for (TicketType type : ticketTypeRepository.findByTicketId(ticketId)) {
            Integer typeId = type.getTypeId();
            String remainingKey = String.format(TicketConstant.TICKET_REMAINING_KEY, typeId);
            String remaining = stringRedisTemplate.opsForValue().get(remainingKey);
            if (remaining == null) {
                continue;
            }
            stringRedisTemplate.delete(Arrays.asList(
                    String.format(TicketConstant.TICKET_TYPE_INFO_KEY, typeId),
                    remainingKey,
                    String.format(TicketConstant.TICKET_SEATS_KEY, typeId)));
            if (Integer.parseInt(remaining) <= 0) {
                LOCAL_REMAINING.put(typeId, new AtomicInteger(0));
            } else {
                LOCAL_REMAINING.remove(typeId);
            }
            closed++;
        }
        log.info("[Admin] 關閉預熱 ticketId={} 票種 {} 個 reason={}", ticketId, closed, reason);
        return closed == 0
                ? String.format("活動「%s」目前未預熱", ticket.getTicketName())
                : String.format("活動「%s」已關閉預熱（%s），共 %d 個票種停止搶票", ticket.getTicketName(), reason, closed);
    }

    /**
     * 測試用：重置活動票務
     * 刪除該活動所有訂單 → DB 剩餘票數還原 → 清除 Redis → 重新預熱
     */
    public String resetTicket(Integer ticketId) {
        Ticket ticket = getData(ticketId);
        if (ticket == null) return "查無此活動";

        int deleted = ticketOrderRepository.deleteByTicketId(ticketId);
        ticketTypeRepository.resetRemainingByTicketId(ticketId);
        ticketTypeRepository.findByTicketId(ticketId).forEach(type -> evictType(type.getTypeId()));
        log.info("[Admin] 重置活動 ticketId={} 刪除訂單 {} 筆", ticketId, deleted);

        String initMsg = initTickets(ticket.getTicketName());
        return String.format("已重置：刪除訂單 %d 筆，%s", deleted, initMsg);
    }

    // ────────────────── 管理用途維持 RedisTemplate<String, Object>（存放 Ticket 物件）──────────────────
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    
    /** 管理用：手動存入 Ticket 物件快取 */
    public void saveToRedis(Ticket ticket) {
        String key = String.format(TicketConstant.TICKET_ID_KEY, ticket.getTicketId());
        redisTemplate.opsForValue().set(key, ticket);
    }
    
    /** 管理用：依 ID 取出 Ticket 物件 */
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
    
    /** 管理用：依 ID 刪除快取 */
    public void deleteFromRedis(Long id) {
        String key = String.format(TicketConstant.TICKET_ID_KEY, id);
        redisTemplate.delete(key);
    }
    
}
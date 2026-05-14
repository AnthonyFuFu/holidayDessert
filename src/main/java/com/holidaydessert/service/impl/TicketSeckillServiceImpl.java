package com.holidaydessert.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.repository.TicketRepository;
import com.holidaydessert.service.TicketSeckillService;
import com.holidaydessert.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class TicketSeckillServiceImpl implements TicketSeckillService {

    // ─────────────────────────────────────────
    // ✅ Redis Key 設計（統一規範，兩把 Key 各司其職）
    // ─────────────────────────────────────────
    private static final String TICKET_INFO_KEY  = "ticket:%s:info";    // 票券完整物件（票名、價格、場次）
    private static final String TICKET_COUNT_KEY = "ticket:%s:count";   // 票數（專供 Lua 原子扣減）
    private static final String INIT_LOCK_KEY    = "ticket:%s:initLock"; // 初始化分布式鎖

    // ─────────────────────────────────────────
    // JUC 元件
    // ─────────────────────────────────────────

    /** Layer 1：Semaphore 流控，限制同時進入 Redis 的請求數 */
    private final Semaphore semaphore = new Semaphore(200);

    /** Layer 2：本地快速失敗，售罄後不打 Redis */
    private final AtomicInteger localTicketCount = new AtomicInteger(0);

    /** Layer 4：異步 DB 持久化線程池 */
    private final ExecutorService dbWritePool = new ThreadPoolExecutor(
        5, 20,
        60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(5000),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

    // ─────────────────────────────────────────
    // Redis Lua 原子扣票腳本（預編譯）
    // ─────────────────────────────────────────
    private static final DefaultRedisScript<Long> DECR_SCRIPT;
    static {
        DECR_SCRIPT = new DefaultRedisScript<>();
        DECR_SCRIPT.setScriptText(
            "local count = redis.call('GET', KEYS[1]) " +
            "if count and tonumber(count) > 0 then " +
            "    return redis.call('DECR', KEYS[1]) " +
            "else " +
            "    return -1 " +
            "end"
        );
        DECR_SCRIPT.setResultType(Long.class);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // ─────────────────────────────────────────
    // ✅ Step 1【開賣前】：預熱 Redis（DB → Redis）
    //    同時存入：票券完整資訊 + 票數
    // ─────────────────────────────────────────
    @Override
    public String initTickets(String event, int ticketCount) {
        String lockKey   = String.format(INIT_LOCK_KEY, event);
        String lockValue = UUID.randomUUID().toString();

        if (!redisLockUtil.lock(lockKey, lockValue, 30)) {
            return "初始化進行中，請勿重複操作";
        }

        try {
            // ✅ 1. 從 DB 撈取票券完整資訊（你原本的邏輯就在這裡！）
            Ticket ticket = ticketRepository.findByTicketEvent(event);
            if (ticket == null) {
                return "找不到該活動的票券：" + event;
            }

            // ✅ 2. 將完整票券資訊存入 Redis
            //       搶票成功後可從這裡取出票券資訊，用於金流/訂單建立
            String infoKey = String.format(TICKET_INFO_KEY, event);
            redisTemplate.opsForValue().set(infoKey, ticket);
            redisTemplate.expire(infoKey, 24, TimeUnit.HOURS);

            // ✅ 3. 將票數獨立存一份（專供 Lua Script 原子扣減，效能最佳）
            String countKey = String.format(TICKET_COUNT_KEY, event);
            redisTemplate.opsForValue().set(countKey, ticketCount);
            redisTemplate.expire(countKey, 24, TimeUnit.HOURS);

            // ✅ 4. 同步 JUC 本地計數器
            localTicketCount.set(ticketCount);

            log.info("[Init] ✅ 預熱完成 event={}, ticketCount={}", event, ticketCount);
            return "票池初始化成功，票數：" + ticketCount;

        } finally {
            redisLockUtil.unlock(lockKey, lockValue);
        }
    }

    // ─────────────────────────────────────────
    // ✅ Step 2【搶票中】：四層防護
    // ─────────────────────────────────────────
    @Override
    public String purchaseTicket(String event, String userId) {

        // Layer 1：JUC Semaphore 流控
        if (!semaphore.tryAcquire()) {
            log.warn("[Layer1-Reject] 系統繁忙 userId={}", userId);
            return "系統繁忙，請稍後再試";
        }

        try {
            // Layer 2：AtomicInteger 本地快速失敗
            if (localTicketCount.get() <= 0) {
                log.info("[Layer2-FastFail] 本地判斷售罄 userId={}", userId);
                return "票已售罄";
            }

            // Layer 3：Redis Lua 原子性扣票
            String countKey = String.format(TICKET_COUNT_KEY, event);
            Long remaining = redisTemplate.execute(
                DECR_SCRIPT,
                Collections.singletonList(countKey)
            );

            if (remaining == null || remaining < 0) {
                localTicketCount.set(0);
                log.info("[Layer3-SoldOut] Redis 確認售罄 userId={}", userId);
                return "票已售罄";
            }

            // 更新本地計數器
            localTicketCount.set(remaining.intValue());

            // ✅ 搶票成功後，從 Redis 取出票券完整資訊（用於金流/訂單）
            String infoKey = String.format(TICKET_INFO_KEY, event);
            Ticket ticketInfo = objectMapper.convertValue(
                redisTemplate.opsForValue().get(infoKey), Ticket.class
            );

            if (ticketInfo != null) {
                log.info("[Purchase] 票券資訊：event={}, price={}", 
                    ticketInfo.getTicketEvent(), ticketInfo.getTicketQuantity());
                // TODO: 金流處理（帶入 ticketInfo 中的票價等資訊）
            }

            // Layer 4：異步 DB 持久化（不阻塞主流程）
            final Long finalRemaining = remaining;
            dbWritePool.submit(() -> {
                try {
                    int affected = ticketRepository.decrementTicketCount(event);
                    if (affected == 0) {
                        log.warn("[Layer4-DB] ⚠️ DB 扣庫存失敗（可能已售罄）event={} userId={}",
                            event, userId);
                        // TODO: 補償機制（推入 MQ 重試）
                    } else {
                        log.info("[Layer4-DB] ✅ DB 扣庫存成功 event={} userId={} remaining={}",
                            event, userId, finalRemaining);
                    }
                } catch (Exception e) {
                    log.error("[Layer4-DB] ❌ 例外錯誤 event={} userId={}", event, userId, e);
                }
            });

            log.info("[Success] 搶票成功 event={} userId={} remaining={}", event, userId, remaining);
            return "搶票成功！剩餘票數：" + remaining;

        } finally {
            semaphore.release();
        }
    }

    @Override
    public int remainCount(String event) {
        String countKey = String.format(TICKET_COUNT_KEY, event);
        Object count = redisTemplate.opsForValue().get(countKey);
        return count != null ? Integer.parseInt(count.toString()) : 0;
    }
}
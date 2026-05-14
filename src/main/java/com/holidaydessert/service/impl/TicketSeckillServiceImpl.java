package com.holidaydessert.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.constant.TicketConstant;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.repository.TicketRepository;
import com.holidaydessert.service.TicketSeckillService;
import com.holidaydessert.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class TicketSeckillServiceImpl implements TicketSeckillService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private RedisLockUtil redisLockUtil;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private ObjectMapper objectMapper;

	// ─────────────────────────────────────────
	// JUC 元件
	// ─────────────────────────────────────────
	/** Layer 1：Semaphore 流控，限制同時進入 Redis 的請求數 */
	private final Semaphore SEMAPHORE = new Semaphore(200);
	/** Layer 2：本地快速失敗，售罄後不打 Redis */
	private final AtomicInteger LOCAL_TICKET_COUNT = new AtomicInteger(0);
	/** Layer 4：異步 DB 持久化線程池 */
	private final ExecutorService DB_WRITE_POOL = new ThreadPoolExecutor(5, 20, 60L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(5000), new ThreadPoolExecutor.CallerRunsPolicy());

	// ─────────────────────────────────────────
	// ✅ Step 1【開賣前】：預熱 Redis（DB → Redis）
	// 同時存入：票券完整資訊 + 票數
	// ─────────────────────────────────────────
	@Override
	public String initTickets(String ticketName, int ticketCount) {
		String lockKey = String.format(TicketConstant.INIT_LOCK_KEY, ticketName);
		String lockValue = UUID.randomUUID().toString();

		if (!redisLockUtil.lock(lockKey, lockValue, 30)) {
			return "初始化進行中，請勿重複操作";
		}

		try {
			// ✅ 1. 從 DB 撈取票券完整資訊（你原本的邏輯就在這裡！）
			Ticket ticket = ticketRepository.findByTicketName(ticketName);
			if (ticket == null) {
				return "找不到該活動的票券：" + ticketName;
			}

			// ✅ 2. 將完整票券資訊存入 Redis
			// 搶票成功後可從這裡取出票券資訊，用於金流/訂單建立
			String infoKey = String.format(TicketConstant.TICKET_INFO_KEY, ticketName);
			redisTemplate.opsForValue().set(infoKey, ticket);
			redisTemplate.expire(infoKey, 24, TimeUnit.HOURS);

			// ✅ 3. 將票數獨立存一份（專供 Lua Script 原子扣減，效能最佳）
			String countKey = String.format(TicketConstant.TICKET_COUNT_KEY, ticketName);
			redisTemplate.opsForValue().set(countKey, ticketCount);
			redisTemplate.expire(countKey, 24, TimeUnit.HOURS);

			// ✅ 4. 同步 JUC 本地計數器
			LOCAL_TICKET_COUNT.set(ticketCount);

			log.info("[Init] ✅ 預熱完成 event={}, ticketCount={}", ticketName, ticketCount);
			return "票池初始化成功，票數：" + ticketCount;

		} finally {
			redisLockUtil.unlock(lockKey, lockValue);
		}
	}

	// ─────────────────────────────────────────
	// ✅ Step 2【搶票中】：四層防護
	// ─────────────────────────────────────────
	@Override
	public String purchaseTicket(String ticketName, Integer userId) {

		// Layer 1：JUC Semaphore 流控
		if (!SEMAPHORE.tryAcquire()) {
			log.warn("[Layer1-Reject] 系統繁忙 userId={}", userId);
			return "系統繁忙，請稍後再試";
		}

		try {
			// Layer 2：AtomicInteger 本地快速失敗
			if (LOCAL_TICKET_COUNT.get() <= 0) {
				log.info("[Layer2-FastFail] 本地判斷售罄 userId={}", userId);
				return "票已售罄";
			}

			// Layer 3：Redis Lua 原子性扣票
			String countKey = String.format(TicketConstant.TICKET_COUNT_KEY, ticketName);
			Long remaining = redisTemplate.execute(TicketConstant.DECR_SCRIPT, Collections.singletonList(countKey));

			if (remaining == null || remaining < 0) {
				LOCAL_TICKET_COUNT.set(0);
				log.info("[Layer3-SoldOut] Redis 確認售罄 userId={}", userId);
				return "票已售罄";
			}

			// 更新本地計數器
			LOCAL_TICKET_COUNT.set(remaining.intValue());

			// ✅ 搶票成功後，從 Redis 取出票券完整資訊（用於金流/訂單）
			String infoKey = String.format(TicketConstant.TICKET_INFO_KEY, ticketName);
			Ticket ticketInfo = objectMapper.convertValue(redisTemplate.opsForValue().get(infoKey), Ticket.class);

			if (ticketInfo != null) {
				log.info("[Purchase] 票券資訊：ticketName={}, price={}", ticketInfo.getTicketName(),ticketInfo.getTicketQuantity());
				// TODO: 金流處理（帶入 ticketInfo 中的票價等資訊）
			}

			// Layer 4：異步 DB 持久化（不阻塞主流程）
			final Long finalRemaining = remaining;
			DB_WRITE_POOL.submit(() -> {
				try {
					int affected = ticketRepository.decrementTicketCount(ticketName);
					if (affected == 0) {
						log.warn("[Layer4-DB] ⚠️ DB 扣庫存失敗（可能已售罄）ticketName={} userId={}", ticketName, userId);
						// TODO: 補償機制（推入 MQ 重試）
					} else {
						log.info("[Layer4-DB] ✅ DB 扣庫存成功 ticketName={} userId={} remaining={}", ticketName, userId,
								finalRemaining);
					}
				} catch (Exception e) {
					log.error("[Layer4-DB] ❌ 例外錯誤 ticketName={} userId={}", ticketName, userId, e);
				}
			});

			log.info("[Success] 搶票成功 ticketName={} userId={} remaining={}", ticketName, userId, remaining);
			return "搶票成功！剩餘票數：" + remaining;

		} finally {
			SEMAPHORE.release();
		}
	}

	@Override
	public int remainCount(String ticketName) {
		String countKey = String.format(TicketConstant.TICKET_COUNT_KEY, ticketName);
		Object count = redisTemplate.opsForValue().get(countKey);
		return count != null ? Integer.parseInt(count.toString()) : 0;
	}

}

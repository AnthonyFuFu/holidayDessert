package com.holidaydessert.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.model.Member;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.model.TicketType;
import com.holidaydessert.repository.MemberRepository;
import com.holidaydessert.repository.TicketTypeRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 後台「搶票測試」：模擬多名會員同時搶票，驗證 Semaphore / 本地計數 / Redis Lua / 配位 是否正確
 *
 * 測試會員帳號以 tkt_sim_ 開頭（停權狀態，無法登入），第一次執行時自動建立，之後重複使用
 */
@Slf4j
@Service
public class TicketSimulateService {

	public static final int MAX_USERS = 2000;
	private static final int MAX_THREADS = 200;
	private static final int TIMEOUT_SECONDS = 60;
	private static final int SAMPLE_SIZE = 10;
	private static final String TEST_ACCOUNT_PREFIX = "tkt_sim_";

	@Autowired
	private TicketService ticketService;

	@Autowired
	private TicketTypeRepository ticketTypeRepository;

	@Autowired
	private MemberRepository memberRepository;

	/**
	 * @param ticketId 活動 ID
	 * @param typeId   指定分區，null 或 0 代表每位會員隨機挑選分區
	 * @param users    同時搶票人數
	 * @param quantity 每人購買張數，0 代表隨機 1 ~ 每人上限
	 */
	public Map<String, Object> simulate(Integer ticketId, Integer typeId, int users, int quantity) throws InterruptedException {
		Map<String, Object> result = new LinkedHashMap<>();

		Ticket ticket = ticketService.getData(ticketId);
		if (ticket == null) {
			result.put("error", "查無此活動");
			return result;
		}
		List<TicketType> types = ticketTypeRepository.findByTicketId(ticketId).stream()
				.filter(type -> typeId == null || typeId == 0 || type.getTypeId().equals(typeId))
				.toList();
		if (types.isEmpty()) {
			result.put("error", "此活動尚無可搶的分區");
			return result;
		}

		int userCount = Math.max(1, Math.min(users, MAX_USERS));
		// 確保已預熱（已存在的剩餘數不會被覆蓋）
		String initMsg = ticketService.initTickets(ticket.getTicketName());
		List<Integer> memIds = prepareTestMembers(userCount);

		AtomicInteger success = new AtomicInteger();
		AtomicInteger soldOut = new AtomicInteger();
		AtomicInteger limitExceeded = new AtomicInteger();
		AtomicInteger busy = new AtomicInteger();
		AtomicInteger failed = new AtomicInteger();
		AtomicInteger ticketsSold = new AtomicInteger();
		ConcurrentLinkedQueue<String> samples = new ConcurrentLinkedQueue<>();

		ExecutorService pool = Executors.newFixedThreadPool(Math.min(userCount, MAX_THREADS));
		CountDownLatch startGate = new CountDownLatch(1);
		CountDownLatch done = new CountDownLatch(userCount);

		for (int i = 0; i < userCount; i++) {
			Integer memId = memIds.get(i);
			TicketType type = types.get(ThreadLocalRandom.current().nextInt(types.size()));
			int buy = quantity > 0 ? quantity : ThreadLocalRandom.current().nextInt(1, type.getTypeMaxPerPerson() + 1);
			pool.submit(() -> {
				try {
					startGate.await(); // 全部就緒後同時開搶
					String msg = ticketService.purchaseTicket(type.getTypeId(), memId, buy);
					if (msg.contains("搶票成功")) {
						success.incrementAndGet();
						ticketsSold.addAndGet(buy);
						if (samples.size() < SAMPLE_SIZE) samples.add(type.getTypeName() + "｜會員 " + memId + "｜" + msg);
					} else if (msg.contains("售罄")) {
						soldOut.incrementAndGet();
					} else if (msg.contains("上限")) {
						limitExceeded.incrementAndGet();
					} else if (msg.contains("繁忙")) {
						busy.incrementAndGet();
					} else {
						failed.incrementAndGet();
					}
				} catch (Exception e) {
					failed.incrementAndGet();
					log.error("[Simulate] 搶票例外 typeId={} memId={}", type.getTypeId(), memId, e);
				} finally {
					done.countDown();
				}
			});
		}

		long begin = System.nanoTime();
		startGate.countDown();
		boolean finished = done.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
		long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - begin);
		pool.shutdownNow();

		result.put("initMessage", initMsg);
		result.put("finished", finished);
		result.put("users", userCount);
		result.put("zones", types.size());
		result.put("success", success.get());
		result.put("soldOut", soldOut.get());
		result.put("limitExceeded", limitExceeded.get());
		result.put("busy", busy.get());
		result.put("failed", failed.get());
		result.put("ticketsSold", ticketsSold.get());
		result.put("elapsedMs", elapsedMs);
		result.put("tps", elapsedMs > 0 ? Math.round(userCount * 1000.0 / elapsedMs) : userCount);
		result.put("samples", new ArrayList<>(samples));
		log.info("[Simulate] ticketId={} {}", ticketId, result);
		return result;
	}

	/** 取得測試會員，不足時自動建立 */
	private List<Integer> prepareTestMembers(int count) {
		List<Integer> memIds = new ArrayList<>(memberRepository.findIdsByAccountPrefix(TEST_ACCOUNT_PREFIX));
		if (memIds.size() >= count) {
			return memIds.subList(0, count);
		}
		String runId = String.valueOf(System.currentTimeMillis());
		List<Member> newMembers = new ArrayList<>();
		for (int i = memIds.size(); i < count; i++) {
			String account = TEST_ACCOUNT_PREFIX + runId + "_" + i;
			newMembers.add(Member.builder()
					.memName("搶票測試會員_" + i)
					.memAccount(account)
					.memPassword("testpwd")
					.memEmail(account + "@test.com")
					.memStatus("0")              // 停權：測試帳號不可登入
					.memVerificationStatus("0")
					.build());
		}
		memberRepository.saveAll(newMembers).forEach(member -> memIds.add(member.getMemId()));
		log.info("[Simulate] 建立測試會員 {} 位，共 {} 位", newMembers.size(), memIds.size());
		return memIds;
	}

}

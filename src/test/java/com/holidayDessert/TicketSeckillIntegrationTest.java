package com.holidayDessert;

import com.holidaydessert.constant.TicketConstant;
import com.holidaydessert.model.Member;
import com.holidaydessert.repository.MemberRepository;
import com.holidaydessert.repository.TicketOrderRepository;
import com.holidaydessert.repository.TicketTypeRepository;
import com.holidaydessert.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // ← 允許非 static 的 @BeforeAll/@AfterAll
public class TicketSeckillIntegrationTest {

    @Autowired
    private TicketService ticketSeckillService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TicketOrderRepository ticketOrderRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    // ───────────────────────────── 測試常數 ─────────────────────────────
    private static final String  TICKET_NAME  = "五月天演唱會";
    private static final Integer TYPE_ROCK    = 1;   // 搖滾區 100張
    private static final Integer TYPE_STAND   = 2;   // 看台區 300張
    private static final Integer TYPE_GENERAL = 3;   // 一般票 250張
    private static final Integer TYPE_FREE    = 4;   // 免費票 500張
    private static final int     TEST_MEMBER_COUNT = 200; // TC06 需要 200 個測試用戶

    /** 測試過程中建立的 Member ID，供 @AfterAll 清除 */
    private final List<Integer> testMemberIds = new ArrayList<>();

    // ═══════════════════════════════════════════════════════════════════
    // @BeforeAll：所有測試共用，建立測試 Member（只跑一次）
    // ═══════════════════════════════════════════════════════════════════
    @BeforeAll
    void createTestMembers() {
        log.info("[BeforeAll] 建立 {} 個測試 Member...", TEST_MEMBER_COUNT);
        // ✅ 加入時間戳記前綴，避免重複跑測試時 UNIQUE 欄位衝突
        String runId = String.valueOf(System.currentTimeMillis());

        for (int i = 0; i < TEST_MEMBER_COUNT; i++) {
            Member member = Member.builder()
                    .memName("測試用戶_" + i)
                    .memAccount("tkt_" + runId + "_" + i)          // ✅ 加 runId 保證唯一
                    .memPassword("testpwd")
                    .memEmail("tkt_" + runId + "_" + i + "@test.com") // ✅ 補上 NOT NULL 欄位，且唯一
                    .memStatus("1")
                    .memVerificationStatus("1")
                    .build();

            Member saved = memberRepository.save(member);
            testMemberIds.add(saved.getMemId());
        }
        log.info("[BeforeAll] ✅ 建立完成，共 {} 個，ID 範圍：{} ~ {}",
                testMemberIds.size(),
                testMemberIds.get(0),
                testMemberIds.get(testMemberIds.size() - 1));
    }

    // ═══════════════════════════════════════════════════════════════════
    // @BeforeEach：每個測試前重置狀態
    // ═══════════════════════════════════════════════════════════════════
    @BeforeEach
    void resetState() throws InterruptedException {
        // Step 1：等待上一個測試的 @Async 寫入完成（最多等 2 秒）
        Thread.sleep(2000);

        // Step 2：刪除所有測試產生的訂單（避免污染下一個測試）
        //         注意：要先刪 ticket_order，才能刪 member（FK 關係）
        ticketOrderRepository.deleteAll();
        log.info("[BeforeEach] 清除訂單完成");

        // Step 3：重置 DB 的 TYPE_REMAINING 回初始值（TYPE_QUANTITY）
        //         因為 @Async 可能已把 DB 庫存扣掉
        ticketTypeRepository.resetRemainingByTicketName(TICKET_NAME);
        log.info("[BeforeEach] DB 庫存 reset 完成");

        // Step 4：清除所有 Redis ticket 相關 Key
        Set<String> keys = redisTemplate.keys("ticket:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("[BeforeEach] 清除 Redis Key：{} 個", keys.size());
        }

        // Step 5：從（已重置的）DB 重新預熱 Redis
        String initResult = ticketSeckillService.initTickets(TICKET_NAME);
        log.info("[BeforeEach] 預熱結果：{}", initResult);
    }

    // ═══════════════════════════════════════════════════════════════════
    // @AfterAll：所有測試結束後，清除測試 Member
    // ═══════════════════════════════════════════════════════════════════
    @AfterAll
    void cleanupTestMembers() throws InterruptedException {
        // 等最後一個測試的 @Async 完成
        Thread.sleep(2000);

        // 先刪訂單（FK 約束：ticket_order → member）
        ticketOrderRepository.deleteAll();

        // 再刪測試 Member
        memberRepository.deleteAllById(testMemberIds);
        log.info("[AfterAll] ✅ 清除 {} 個測試 Member 完成", testMemberIds.size());
    }

    // ═══════════════════════════════════════════════════════════════════
    // 測試一：初始化驗證
    // ═══════════════════════════════════════════════════════════════════
    @Test
    @Order(1)
    @DisplayName("✅ TC01 - 初始化：DB → Redis 三層結構正確建立")
	void testInitTickets() {
		String infoKey = String.format(TicketConstant.TICKET_TYPE_INFO_KEY, TYPE_ROCK);
		assertTrue(redisTemplate.hasKey(infoKey), "ticket:type:info:1 應存在");

		assertEquals("搖滾區", redisTemplate.opsForHash().get(infoKey, TicketConstant.FIELD_TYPE_NAME), "票種名稱應為搖滾區");
		assertEquals("3200", redisTemplate.opsForHash().get(infoKey, TicketConstant.FIELD_TYPE_PRICE), "搖滾區票價應為 3200");
		assertEquals("4", redisTemplate.opsForHash().get(infoKey, TicketConstant.FIELD_TYPE_MAX_PER_PERSON), "每人購買上限應為 4");

		String remainingKey = String.format(TicketConstant.TICKET_REMAINING_KEY, TYPE_ROCK);
		assertEquals("100", redisTemplate.opsForValue().get(remainingKey), "搖滾區初始剩餘應為 100");

		// ticket:user:count 不應預先存在
		assertFalse(redisTemplate.hasKey(String.format(TicketConstant.TICKET_USER_COUNT_KEY, TYPE_ROCK, testMemberIds.get(0))), "user:count 不應預先存在");
		assertTrue(redisTemplate.hasKey(String.format(TicketConstant.TICKET_REMAINING_KEY, TYPE_STAND)));
		assertTrue(redisTemplate.hasKey(String.format(TicketConstant.TICKET_REMAINING_KEY, TYPE_GENERAL)));
		assertTrue(redisTemplate.hasKey(String.format(TicketConstant.TICKET_REMAINING_KEY, TYPE_FREE)));

		log.info("[TC01] ✅ 初始化驗證通過");
	}

    // ═══════════════════════════════════════════════════════════════════
    // 測試二：正常搶票
    // ═══════════════════════════════════════════════════════════════════
    @Test
    @Order(2)
    @DisplayName("✅ TC02 - 正常搶票：購買成功，剩餘票數正確扣減")
    void testPurchaseSuccess() {
        Integer memId = testMemberIds.get(0); // ✅ 使用真實存在的 Member
        String result = ticketSeckillService.purchaseTicket(TYPE_ROCK, memId, 2);

        assertTrue(result.contains("搶票成功"), "應回傳搶票成功，實際：" + result);
        assertTrue(result.contains("98"),        "剩餘應為 98，實際：" + result);
        assertEquals(98, ticketSeckillService.remainCount(TYPE_ROCK));

        String userKey = String.format(TicketConstant.TICKET_USER_COUNT_KEY, TYPE_ROCK, memId);
        assertEquals("2", redisTemplate.opsForValue().get(userKey), "已購數量應為 2");

        log.info("[TC02] ✅ 正常搶票通過，result={}", result);
    }

    // ═══════════════════════════════════════════════════════════════════
    // 測試三：超過個人上限（單次購買）
    // ═══════════════════════════════════════════════════════════════════
    @Test
    @Order(3)
    @DisplayName("❌ TC03 - 超過個人上限：單次購買超過 maxPerPerson")
    void testPersonalLimitExceeded_SinglePurchase() {
        Integer memId = testMemberIds.get(0); // ✅ 使用真實存在的 Member
        String result = ticketSeckillService.purchaseTicket(TYPE_ROCK, memId, 5);

        assertTrue(result.contains("個人購買上限"), "應回傳超過個人購買上限，實際：" + result);
        assertEquals(100, ticketSeckillService.remainCount(TYPE_ROCK), "購買失敗，剩餘應仍為 100");

        log.info("[TC03] ✅ 個人上限（單次）驗證通過，result={}", result);
    }

    // ═══════════════════════════════════════════════════════════════════
    // 測試四：超過個人上限（累積購買）
    // ═══════════════════════════════════════════════════════════════════
    @Test
    @Order(4)
    @DisplayName("❌ TC04 - 超過個人上限：累積購買超過 maxPerPerson")
    void testPersonalLimitExceeded_Cumulative() {
        Integer memId = testMemberIds.get(1); // ✅ 使用真實存在的 Member（與 TC03 不同人）
        String first = ticketSeckillService.purchaseTicket(TYPE_ROCK, memId, 3);
        assertTrue(first.contains("搶票成功"), "第一次應成功，實際：" + first);

        String second = ticketSeckillService.purchaseTicket(TYPE_ROCK, memId, 2);
        assertTrue(second.contains("個人購買上限"), "累積超限應失敗，實際：" + second);

        assertEquals(97, ticketSeckillService.remainCount(TYPE_ROCK), "剩餘應為 97（只扣第一次 3 張）");

        String userKey = String.format(TicketConstant.TICKET_USER_COUNT_KEY, TYPE_ROCK, memId);
        assertEquals("3", redisTemplate.opsForValue().get(userKey), "已購數量應仍為 3");

        log.info("[TC04] ✅ 個人上限（累積）驗證通過");
    }

    // ═══════════════════════════════════════════════════════════════════
    // 測試五：票售罄防護
    // ═══════════════════════════════════════════════════════════════════
    @Test
    @Order(5)
    @DisplayName("❌ TC05 - 票售罄：剩餘 1 張，第二位使用者買不到")
    void testSoldOut() {
        Integer memA = testMemberIds.get(2); // ✅ 使用真實存在的 Member
        Integer memB = testMemberIds.get(3);

        // 強制將 Redis 剩餘設為 1（模擬快售罄）
        String remainingKey = String.format(TicketConstant.TICKET_REMAINING_KEY, TYPE_ROCK);
        redisTemplate.opsForValue().set(remainingKey, "1");

        String resultA = ticketSeckillService.purchaseTicket(TYPE_ROCK, memA, 1);
        assertTrue(resultA.contains("搶票成功"), "用戶 A 應搶票成功，實際：" + resultA);
        assertTrue(resultA.contains("0"),        "剩餘應為 0，實際：" + resultA);

        String resultB = ticketSeckillService.purchaseTicket(TYPE_ROCK, memB, 1);
        assertTrue(resultB.contains("售罄"), "用戶 B 應回傳售罄，實際：" + resultB);

        assertEquals(0, ticketSeckillService.remainCount(TYPE_ROCK), "剩餘不能為負數");

        log.info("[TC05] ✅ 售罄防護驗證通過");
    }

    // ═══════════════════════════════════════════════════════════════════
    // 測試六：高併發（最重要）
    // ═══════════════════════════════════════════════════════════════════
    @Test
    @Order(6)
    @DisplayName("🔥 TC06 - 高併發：200 人同時搶 100 張搖滾區票，驗證不超賣")
    void testConcurrentPurchase() throws InterruptedException {
        int totalUsers   = 200;
        int totalTickets = 100;

        CountDownLatch startLatch   = new CountDownLatch(1);
        CountDownLatch doneLatch    = new CountDownLatch(totalUsers);
        AtomicInteger successCount  = new AtomicInteger(0);
        AtomicInteger soldOutCount  = new AtomicInteger(0);
        AtomicInteger busyCount     = new AtomicInteger(0);
        AtomicInteger errorCount    = new AtomicInteger(0);

        ExecutorService pool = Executors.newFixedThreadPool(totalUsers);

        for (int i = 0; i < totalUsers; i++) {
            final Integer memId = testMemberIds.get(i); // ✅ 使用真實 Member ID
            pool.submit(() -> {
                try {
                    startLatch.await();
                    String result = ticketSeckillService.purchaseTicket(TYPE_ROCK, memId, 1);

                    if      (result.contains("搶票成功"))  successCount.incrementAndGet();
                    else if (result.contains("售罄"))      soldOutCount.incrementAndGet();
                    else if (result.contains("繁忙"))      busyCount.incrementAndGet();
                    else                                   errorCount.incrementAndGet();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    errorCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 起跑！

        boolean completed = doneLatch.await(30, TimeUnit.SECONDS);
        pool.shutdown();

        log.info("══════════════════════════════════════════");
        log.info("[TC06] 高併發測試結果");
        log.info("  搶票成功：{} 人",  successCount.get());
        log.info("  票已售罄：{} 人",  soldOutCount.get());
        log.info("  系統繁忙：{} 人",  busyCount.get());
        log.info("  例外錯誤：{} 人",  errorCount.get());
        log.info("  Redis 剩餘：{} 張", ticketSeckillService.remainCount(TYPE_ROCK));
        log.info("══════════════════════════════════════════");

        assertTrue(completed, "所有 Thread 應在 30 秒內完成");
        assertEquals(0, errorCount.get(), "不應有例外錯誤");

        // 🔑 核心：不能超賣
        assertTrue(successCount.get() <= totalTickets, String.format("❌ 超賣！成功 %d 人 > 票數 %d 張", successCount.get(), totalTickets));

        // 🔑 應精確售出 100 張
        assertEquals(totalTickets, successCount.get(), String.format("成功人數應為 %d，實際為 %d", totalTickets, successCount.get()));

        // 🔑 Redis 剩餘應為 0
        assertEquals(0, ticketSeckillService.remainCount(TYPE_ROCK), "Redis 剩餘票數應為 0");

        assertEquals(totalUsers, successCount.get() + soldOutCount.get() + busyCount.get(), "所有請求都應有明確回應");

        log.info("[TC06] ✅ 高併發驗證通過：100 張票精確售出，無超賣");
    }

    // ═══════════════════════════════════════════════════════════════════
    // 測試七：重複初始化防護
    // ═══════════════════════════════════════════════════════════════════
    @Test
    @Order(7)
    @DisplayName("✅ TC07 - 分布式鎖防重複初始化")
    void testDuplicateInitPrevented() throws InterruptedException {
        int threadCount = 5;
        CountDownLatch startLatch  = new CountDownLatch(1);
        CountDownLatch doneLatch   = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger skipCount    = new AtomicInteger(0);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            pool.submit(() -> {
                try {
                    startLatch.await();
                    String result = ticketSeckillService.initTickets(TICKET_NAME);
                    if (result.contains("預熱完成"))  successCount.incrementAndGet();
                    else if (result.contains("進行中")) skipCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await(15, TimeUnit.SECONDS);
        pool.shutdown();

        log.info("[TC07] 預熱成功：{}次，跳過：{}次", successCount.get(), skipCount.get());
        assertTrue(successCount.get() >= 1, "至少應有 1 次初始化成功");
        assertEquals(threadCount, successCount.get() + skipCount.get());
    }
}
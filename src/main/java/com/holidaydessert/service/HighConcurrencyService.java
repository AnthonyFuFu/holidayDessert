package com.holidaydessert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HighConcurrencyService {

    @Autowired
    private ConcurrentLinkedQueueService concurrentLinkedQueueService;

    @Autowired
    private ConcurrentListService concurrentListService;

    @Autowired
    private ConcurrentSemaphoreService concurrentSemaphoreService;

    @Autowired
    private LockService lockService;

    // =============================================
    // 初始化票池
    // =============================================
    public String initLinkedQueueTickets(int ticketCount) {
        return concurrentLinkedQueueService.init(ticketCount);
    }
    
    public String initListTickets(int ticketCount) {
        return concurrentListService.init(ticketCount);
    }

    // =============================================
    // 搶票（並發呼叫）
    // =============================================
    public String concurrentLinkedQueueTickets() {
        return concurrentLinkedQueueService.shakedown();
    }
    
    public String concurrentListTickets() {
        return concurrentListService.shakedown();
    }

    // =============================================
    // 查詢剩餘票數
    // =============================================
    public int linkedQueueRemainCount() {
        return concurrentLinkedQueueService.remainCount();
    }
    
    public int listRemainCount() {
        return concurrentListService.remainCount();
    }

    // =============================================
    // Semaphore
    // =============================================
    public String semaphoreTest(int clientTotal, int threadTotal) throws Exception {
        return concurrentSemaphoreService.execute(clientTotal, threadTotal);
    }

    // =============================================
    // Lock vs Synchronized
    // =============================================
    public String lockVsSyncTest(int temp) {
        return lockService.execute(temp);
    }
    
}
package com.holidaydessert.service.impl;

import com.holidaydessert.service.ConcurrentLinkedQueueService;
import com.holidaydessert.service.ConcurrentListService;
import com.holidaydessert.service.ConcurrentSemaphoreService;
import com.holidaydessert.service.HighConcurrencyService;
import com.holidaydessert.service.LockService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HighConcurrencyServiceImpl implements HighConcurrencyService {

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
    @Override
    public String initLinkedQueueTickets(int ticketCount) {
        return concurrentLinkedQueueService.init(ticketCount);
    }

    @Override
    public String initListTickets(int ticketCount) {
        return concurrentListService.init(ticketCount);
    }

    // =============================================
    // 搶票
    // =============================================
    @Override
    public String concurrentLinkedQueueTickets() {
        return concurrentLinkedQueueService.shakedown();
    }

    @Override
    public String concurrentListTickets() {
        return concurrentListService.shakedown();
    }

    // =============================================
    // 查詢剩餘票數
    // =============================================
    @Override
    public int linkedQueueRemainCount() {
        return concurrentLinkedQueueService.remainCount();
    }

    @Override
    public int listRemainCount() {
        return concurrentListService.remainCount();
    }

    @Override
    public String semaphoreTest(int clientTotal, int threadTotal) throws Exception {
        return concurrentSemaphoreService.execute(clientTotal, threadTotal);
    }

    @Override
    public String lockVsSyncTest(int temp) {
        return lockService.execute(temp);
    }
    
}
package com.holidaydessert.service;

public interface HighConcurrencyService {

    // =============================================
    // 初始化票池
    // =============================================
    String initLinkedQueueTickets(int ticketCount);
    String initListTickets(int ticketCount);

    // =============================================
    // 搶票（並發呼叫）
    // =============================================
    String concurrentLinkedQueueTickets();
    String concurrentListTickets();

    // =============================================
    // 查詢剩餘票數
    // =============================================
    int linkedQueueRemainCount();
    int listRemainCount();

    // =============================================
    // Semaphore
    // =============================================
    String semaphoreTest(int clientTotal, int threadTotal) throws Exception;

    // =============================================
    // Lock vs Synchronized
    // =============================================
    String lockVsSyncTest(int temp);
}
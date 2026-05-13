package com.holidaydessert.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConcurrentLinkedQueueService {

    // 票池存在 Service，跨請求共享
    private Queue<String> tickets = new ConcurrentLinkedQueue<>();

    // =============================================
    // 初始化票池（只呼叫一次）
    // =============================================
    public String init(int ticketCount) {
        tickets.clear(); // 重置票池
        for (int i = 1; i <= ticketCount; i++) {
            tickets.add((ticketCount - i) + " 這是第" + i + "張票");
        }
        String result = "ConcurrentLinkedQueue 票池初始化完成，票數：" + ticketCount;
        log.info(result);
        return result;
    }

    // =============================================
    // 搶票（並發呼叫）
    // =============================================
    public String shakedown() {
        String ticket = tickets.poll(); // ConcurrentLinkedQueue 本身線程安全

        if (ticket == null) {
            String str = "線程:[" + Thread.currentThread().getName() + "] 票已售完！剩餘：0";
            log.info(str);
            return str;
        }

        String str = "線程:[" + Thread.currentThread().getName() + "] 搶到：" + ticket + " 剩餘：" + tickets.size();
        log.info(str);
        return str;
    }

    // 查詢剩餘票數
    public int remainCount() {
        return tickets.size();
    }
    
}

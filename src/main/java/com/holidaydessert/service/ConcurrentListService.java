package com.holidaydessert.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConcurrentListService {

    // 票池和計數器存在 Service，跨請求共享
    private List<String> list = Collections.synchronizedList(new ArrayList<>());
    private AtomicInteger count = new AtomicInteger(0);

    // =============================================
    // 初始化票池（只呼叫一次）
    // =============================================
    public String init(int ticketCount) {
        list.clear();
        for (int i = 1; i <= ticketCount; i++) {
            list.add(i + " 這是第" + (ticketCount - i) + "張票");
        }
        count.set(ticketCount); // ✅ 重置計數器
        String result = "ConcurrentList 票池初始化完成，票數：" + ticketCount;
        log.info(result);
        return result;
    }

    // =============================================
    // 搶票（並發呼叫）
    // =============================================
    public String shakedown() {
        synchronized (this) {
            int current = count.decrementAndGet(); // 先 -1

            if (current < 0) {
                count.incrementAndGet(); // 防止計數器無限減少
                String str = "線程:[" + Thread.currentThread().getName() + "] 票已售完！剩餘：0";
                log.info(str);
                return str;
            }

            String str = "線程:[" + Thread.currentThread().getName() + "] 搶到：" + list.get(current) + " 剩餘：" + current;
            log.info(str);
            return str;
        }
    }

    // 查詢剩餘票數
    public int remainCount() {
        return Math.max(count.get(), 0);
    }
    
}

package com.holidaydessert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.service.HighConcurrencyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/highConcurrency")
public class HighConcurrencyController {

    @Autowired
    private HighConcurrencyService highConcurrencyService;

    // =============================================
    // 初始化票池
    // POST /highConcurrency/linkedQueueTicket/init?ticketCount=10000
    // POST /highConcurrency/listTicket/init?ticketCount=100
    // =============================================
    @PostMapping("/linkedQueueTicket/init")
    public String initLinkedQueueTicket(
            @RequestParam(defaultValue = "10000") int ticketCount) {
        return highConcurrencyService.initLinkedQueueTickets(ticketCount);
    }

    @PostMapping("/listTicket/init")
    public String initListTicket(
            @RequestParam(defaultValue = "100") int ticketCount) {
        return highConcurrencyService.initListTickets(ticketCount);
    }

    // =============================================
    // 搶票（並發呼叫）
    // GET /highConcurrency/linkedQueueTicket
    // GET /highConcurrency/listTicket
    // =============================================
    @GetMapping("/linkedQueueTicket")
    public String linkedQueueTicket() {
        return highConcurrencyService.concurrentLinkedQueueTickets();
    }

    @GetMapping("/listTicket")
    public String listTicket() {
        return highConcurrencyService.concurrentListTickets();
    }

    // =============================================
    // 查詢剩餘票數
    // GET /highConcurrency/linkedQueueTicket/remain
    // GET /highConcurrency/listTicket/remain
    // =============================================
    @GetMapping("/linkedQueueTicket/remain")
    public String linkedQueueRemain() {
        return "ConcurrentLinkedQueue 剩餘票數：" + highConcurrencyService.linkedQueueRemainCount();
    }

    @GetMapping("/listTicket/remain")
    public String listRemain() {
        return "ConcurrentList 剩餘票數：" + highConcurrencyService.listRemainCount();
    }

    // =============================================
    // Semaphore 並發測試
    // GET /highConcurrency/semaphore?clientTotal=5000&threadTotal=200
    // =============================================
    @GetMapping("/semaphore")
    public String semaphore(
            @RequestParam(defaultValue = "5000") int clientTotal,
            @RequestParam(defaultValue = "200")  int threadTotal) throws Exception {
        return highConcurrencyService.semaphoreTest(clientTotal, threadTotal);
    }

    // =============================================
    // Lock vs Synchronized 效能比較
    // GET /highConcurrency/lockVsSync?temp=1000000
    // =============================================
    @GetMapping("/lockVsSync")
    public String lockVsSync(
            @RequestParam(defaultValue = "1000000") int temp) {
        return highConcurrencyService.lockVsSyncTest(temp);
    }
}

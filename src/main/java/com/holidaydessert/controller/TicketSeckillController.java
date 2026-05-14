package com.holidaydessert.controller;

import com.holidaydessert.service.TicketSeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/seckill")
public class TicketSeckillController {

    @Autowired
    private TicketSeckillService ticketSeckillService;

    /**
     * 初始化票池
     * POST /seckill/init?event=五月天演唱會&count=1000
     */
    @PostMapping("/init")
    public String init(
            @RequestParam String event,
            @RequestParam(defaultValue = "1000") int count) {
        return ticketSeckillService.initTickets(event, count);
    }

    /**
     * 搶票
     * POST /seckill/purchase?event=五月天演唱會&userId=user123
     */
    @PostMapping("/purchase")
    public String purchase(
            @RequestParam String event,
            @RequestParam String userId) {
        return ticketSeckillService.purchaseTicket(event, userId);
    }

    /**
     * 查詢剩餘票數
     * GET /seckill/remain?event=五月天演唱會
     */
    @GetMapping("/remain")
    public String remain(@RequestParam String event) {
        return "剩餘票數：" + ticketSeckillService.remainCount(event);
    }
}
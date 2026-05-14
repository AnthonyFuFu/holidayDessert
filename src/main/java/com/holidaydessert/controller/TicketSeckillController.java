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
     * POST /seckill/init?ticketName=五月天演唱會&count=1000
     */
    @GetMapping("/init")
    public String init(
            @RequestParam String ticketName,
            @RequestParam(defaultValue = "1000") int count) {
        return ticketSeckillService.initTickets(ticketName, count);
    }

    /**
     * 搶票
     * POST /seckill/purchase?ticketName=五月天演唱會&userId=1
     */
    @GetMapping("/purchase")
    public String purchase(
            @RequestParam String ticketName,
            @RequestParam Integer userId) {
        return ticketSeckillService.purchaseTicket(ticketName, userId);
    }

    /**
     * 查詢剩餘票數
     * GET /seckill/remain?ticketName=五月天演唱會
     */
    @GetMapping("/remain")
    public String remain(@RequestParam String ticketName) {
        return "剩餘票數：" + ticketSeckillService.remainCount(ticketName);
    }
}
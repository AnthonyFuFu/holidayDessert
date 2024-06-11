package com.holidaydessert.highconcurrency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.holidaydessert.highconcurrency.juc.ConcurrentLinkedQueueTickets;
import com.holidaydessert.highconcurrency.juc.ConcurrentListTickets;

/**
 * 测试接口
 * @author YI
 * @date 2018-8-10 15:40:31
 */
@Controller
public class TestController {
    @Autowired
    public ConcurrentListTickets concurrentListTickets;
    @Autowired
    public ConcurrentLinkedQueueTickets concurrentLinkedQueueTickets;

    @RequestMapping("/concurrentLinkedQueueTickets")
    @ResponseBody
    public String concurrentLinkedQueueTickets() {
        concurrentLinkedQueueTickets.shakedown();

        return "concurrentLinkedQueueTickets";
    }

    @RequestMapping("/concurrentListTickets")
    @ResponseBody
    public String concurrentListTickets() {
        String shakedown = concurrentListTickets.shakedown();

        return shakedown;
    }
}

package com.holidaydessert.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.TicketDao;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.service.TicketService;
import com.holidaydessert.utils.RedisLockUtil;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketDao ticketDao;

    @Autowired
    private RedisLockUtil redisLockUtil;
    
    @Transactional
    public String purchaseTicket(String event) {
        String lockKey = "ticket:" + event;
        String lockValue = UUID.randomUUID().toString();

        try {
            if (!redisLockUtil.lock(lockKey, lockValue, 30)) {
                return "系统繁忙，请稍后再试";
            }

            Ticket ticket = ticketDao.findByEvent(event);
            if (ticket == null || ticket.getQuantity() <= 0) {
                return "票已售罄";
            }

            ticket.setQuantity(ticket.getQuantity() - 1);
            ticketDao.save(ticket);

            // 金流处理逻辑...

            return "抢票成功";
        } finally {
            redisLockUtil.unlock(lockKey);
        }
    }
    
}

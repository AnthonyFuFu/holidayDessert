package com.holidaydessert.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.TicketDao;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.service.TicketRedisService;
import com.holidaydessert.utils.RedisLockUtil;

@Service
public class TicketRedisServiceImpl implements TicketRedisService {

    private static final String PREFIX = "ticket:";
    
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
                return "系统繁忙，請稍後再試";
            }

            Ticket ticket = ticketDao.findByEvent(event);
            if (ticket == null || ticket.getTicketQuantity() <= 0) {
                return "票已售罄";
            }

            ticket.setTicketQuantity(ticket.getTicketQuantity() - 1);
            ticketDao.save(ticket);

            // 金流处理逻辑...

            return "搶票成功";
        } finally {
            redisLockUtil.unlock(lockKey);
        }
    }
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToRedis(Ticket ticket) {
        String key = PREFIX + ticket.getTicketId();
        redisTemplate.opsForValue().set(key, ticket);
    }

    public Ticket getFromRedis(Long id) {
        String key = PREFIX + id;
        return (Ticket) redisTemplate.opsForValue().get(key);
    }

    public void deleteFromRedis(Long id) {
        String key = PREFIX + id;
        redisTemplate.delete(key);
    }
    
}

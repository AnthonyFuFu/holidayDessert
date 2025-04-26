package com.holidaydessert.service.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper objectMapper;
    
    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Transactional
    public String purchaseTicket(String event) {
    	String lockKey = PREFIX + event + ":lock";  // 使用分布式鎖的 key
        String lockValue = UUID.randomUUID().toString();

        try {
            if (!redisLockUtil.lock(lockKey, lockValue, 30)) {
                return "系统繁忙，請稍後再試";
            }
            // 從 Redis 中取票務資訊
            String ticketKey = PREFIX + event;
            Ticket ticket = objectMapper.convertValue(redisTemplate.opsForValue().get(ticketKey), Ticket.class);
            
            if (ticket == null) {
                // 如果票務信息不存在，從資料庫加載並設置到 Redis
                ticket = ticketDao.findByEvent(event);
                if (ticket == null || ticket.getTicketQuantity() <= 0) {
                    return "票已售罄";
                }
                // 將票務資訊存入 Redis，並設定過期時間
                redisTemplate.opsForValue().set(ticketKey, ticket);
                redisTemplate.expire(ticketKey, 30, TimeUnit.MINUTES); // 可設過期時間
            }
            // 檢查剩餘票數是否足夠
            if (ticket.getTicketQuantity() <= 0) {
                return "票已售罄";
            }
            // 更新剩餘票數
            ticket.setTicketQuantity(ticket.getTicketQuantity() - 1);
            // 更新 Redis 中的票務數量
            redisTemplate.opsForValue().set(ticketKey, ticket);
            
            // 金流處理邏輯...
            
            return "搶票成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "系統錯誤，請稍後再試";
        } finally {
            redisLockUtil.unlock(lockKey);
        }
    }

    public void saveToRedis(Ticket ticket) {
        String key = PREFIX + ticket.getTicketId();
        redisTemplate.opsForValue().set(key, ticket);
    }
    
    public void saveTicketToRedisByEvent(String event) {
        Ticket ticket = ticketDao.findByEvent(event);
        if (ticket != null) {
            saveToRedis(ticket);
        } else {
            throw new RuntimeException("找不到該活動的票券：" + event);
        }
    }
    
    public Ticket getFromRedis(Long id) {
        String key = PREFIX + id;
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;  // 若沒有資料，回傳 null
        }
        try {
            // 將 LinkedHashMap 轉回 Ticket 物件
            return objectMapper.convertValue(obj, Ticket.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // 如果轉換失敗，回傳 null
        }
    }

    public void deleteFromRedis(Long id) {
        String key = PREFIX + id;
        redisTemplate.delete(key);
    }
    
}

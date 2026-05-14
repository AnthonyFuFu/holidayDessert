package com.holidaydessert.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.repository.TicketRepository;
import com.holidaydessert.service.TicketRedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TicketRedisServiceImpl implements TicketRedisService {

    // ✅ 統一 Key 規範（與 TicketSeckillServiceImpl 一致）
    private static final String TICKET_INFO_KEY = "ticket:%s:info";
    private static final String TICKET_ID_KEY   = "ticket:%s";

    @Autowired
    private TicketRepository ticketRepository; // 改用 JPA Repository

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * ✅ 修正：改用 ticketRepository，Key 與 SeckillService 統一
     * 管理用途：手動將指定活動的票券預熱進 Redis
     */
    public void saveTicketToRedisByEvent(String event) {
        Ticket ticket = ticketRepository.findByTicketEvent(event); // ✅ 改這裡
        if (ticket != null) {
            // ✅ 使用統一 Key：ticket:{event}:info
            String infoKey = String.format(TICKET_INFO_KEY, event);
            redisTemplate.opsForValue().set(infoKey, ticket);
            log.info("[Cache] ✅ 票券預熱完成 event={}", event);
        } else {
            throw new RuntimeException("找不到該活動的票券：" + event);
        }
    }

    /** 管理用：用 ID 存 Ticket */
    public void saveToRedis(Ticket ticket) {
        String key = String.format(TICKET_ID_KEY, ticket.getTicketId());
        redisTemplate.opsForValue().set(key, ticket);
    }

    /** 管理用：用 ID 取 Ticket */
    public Ticket getFromRedis(Long id) {
        String key = String.format(TICKET_ID_KEY, id);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) return null;
        try {
            return objectMapper.convertValue(obj, Ticket.class);
        } catch (Exception e) {
            log.error("[Redis] 轉換失敗 id={}", id, e);
            return null;
        }
    }

    /** 管理用：刪除快取 */
    public void deleteFromRedis(Long id) {
        String key = String.format(TICKET_ID_KEY, id);
        redisTemplate.delete(key);
    }
    
}

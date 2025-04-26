package com.holidayDessert;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.holidaydessert.dao.TicketDao;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.service.impl.TicketRedisServiceImpl;
import com.holidaydessert.utils.RedisLockUtil;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TicketRedisServiceImplTest {

    @Mock
    private TicketDao ticketDao;
    
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RedisLockUtil redisLockUtil;
    
    @InjectMocks
    private TicketRedisServiceImpl ticketRedisService;
    
    @Test
    public void testPurchaseTicket() {
        String event = "Christmas Party";

        Ticket ticket = new Ticket();
        ticket.setTicketEvent(event);
        ticket.setTicketQuantity(10);

        // 模擬 Redis 鎖成功
        when(redisLockUtil.lock(anyString(), anyString(), anyLong())).thenReturn(true);

        // 模擬 DAO 查詢與保存
        when(ticketDao.findByEvent(event)).thenReturn(ticket);
        doNothing().when(ticketDao).save(any(Ticket.class));

        String response = ticketRedisService.purchaseTicket(event);

        assertEquals("抢票成功", response);
        verify(ticketDao, times(1)).save(ticket);
        verify(redisLockUtil, times(1)).unlock(anyString());
    }
    
}
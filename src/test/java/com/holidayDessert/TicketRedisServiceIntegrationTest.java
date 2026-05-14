package com.holidayDessert;

import com.holidaydessert.service.TicketSeckillService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 測試完會自動 rollback，不會真的改動資料庫
public class TicketRedisServiceIntegrationTest {

    @Autowired
    private TicketSeckillService ticketSeckillService;

    @Test
    public void testPurchaseRealTicket() {
    	
    	for (int i = 0; i < 100; i++) {
            // 資料庫中已有這筆資料：五月天演唱會
            String ticketName = "五月天演唱會";
            String result = ticketSeckillService.purchaseTicket(ticketName,i);
            assertEquals("搶票成功", result);
		}
    }
}
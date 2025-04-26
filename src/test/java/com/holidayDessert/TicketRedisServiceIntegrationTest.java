package com.holidayDessert;

import com.holidaydessert.service.TicketRedisService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 測試完會自動 rollback，不會真的改動資料庫
public class TicketRedisServiceIntegrationTest {

    @Autowired
    private TicketRedisService ticketRedisService;

    @Test
    public void testPurchaseRealTicket() {
        // 資料庫中已有這筆資料：跨年煙火派對
        String event = "跨年煙火派對";
        String result = ticketRedisService.purchaseTicket(event);
        assertEquals("搶票成功", result);
    }
}
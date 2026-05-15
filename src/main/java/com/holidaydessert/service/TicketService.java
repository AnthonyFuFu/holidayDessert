package com.holidaydessert.service;

import com.holidaydessert.model.Ticket;

public interface TicketService {
	
    /** 售票前預熱：DB → Redis 三層結構（不需要傳 count，從 DB 讀） */
    String initTickets(String ticketName);
    /** 搶票：三層防護 + 異步 DB 持久化 */
    String purchaseTicket(Integer typeId, Integer memId, Integer quantity);
    /** 查詢指定票種剩餘票數 */
    int remainCount(Integer typeId);
    
    /************ 管理用 ************/
    /** 管理用：手動存入 Ticket 物件快取 */
    void saveToRedis(Ticket ticket);
    /** 管理用：依 ID 取出 Ticket 物件 */
    Ticket getFromRedis(Long id);
    /** 管理用：依 ID 刪除快取 */
    void deleteFromRedis(Long id);
    
}
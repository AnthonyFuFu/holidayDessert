package com.holidaydessert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	

    // Spring Data JPA 依 Entity 欄位名自動生成 SELECT 語句
    // 對應原本 EntityManager 的 JPQL：WHERE t.ticketName = :ticketName
    Ticket findByTicketName(String ticketName);

    // 原子性扣庫存（搭配上一版 Layer4 異步 DB 寫入使用）
    // AND ticketQuantity > 0：防止 DB 層再次超賣（雙重保險）
    // 回傳 int：影響筆數，0 代表扣減失敗（已售罄）
    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.ticketQuantity = t.ticketQuantity - 1 WHERE t.ticketName = :ticketName AND t.ticketQuantity > 0")
    int decrementTicketCount(@Param("ticketName") String ticketName);
	
	// Spring Data JPA 自動產生 SELECT * FROM ticket WHERE ticket_name = ?
	
//	  JPA範例
//    Ticket findByTicketName(String ticketName);
    
//    List<Ticket> findByTicketStatus(String status);        // WHERE ticket_status = ?
//    List<Ticket> findByTicketEventAndTicketStatus(...);    // WHERE ... AND ...
//    List<Ticket> findByTicketEventOrderByTicketId(...);    // WHERE ... ORDER BY ...
    
// 	  // 查詢
//    ticketRepository.findById(id);        // 依 ID 查詢
//    ticketRepository.findAll();           // 查全部
//    ticketRepository.count();             // 計算筆數
//    ticketRepository.existsById(id);      // 是否存在
//
//    // 儲存（自動判斷 INSERT 或 UPDATE）
//    ticketRepository.save(ticket);
//    ticketRepository.saveAll(tickets);
//
//    // 刪除
//    ticketRepository.deleteById(id);
//    ticketRepository.delete(ticket);
    
}

package com.holidaydessert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
	// Spring Data JPA 自動產生 SELECT * FROM ticket WHERE ticket_event = ?
	
//	  JPA範例
//    Ticket findByTicketEvent(String ticketEvent);
    
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

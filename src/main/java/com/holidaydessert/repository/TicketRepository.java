package com.holidaydessert.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    boolean existsByTicketName(String ticketName);

    boolean existsByTicketNameAndTicketIdNot(String ticketName, Integer ticketId);

    // =============================================
    // backList：後台 DataTables 分頁（含票種數與剩餘票數加總）
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value = "SELECT * FROM ( " +
                   "  SELECT t.TICKET_ID, t.TICKET_NAME, t.TICKET_VENUE, t.TICKET_QUANTITY, t.TICKET_STATUS, " +
                   "         DATE_FORMAT(t.TICKET_START,      '%Y-%m-%d %H:%i') TICKET_START, " +
                   "         DATE_FORMAT(t.TICKET_SALE_START, '%Y-%m-%d %H:%i') TICKET_SALE_START, " +
                   "         DATE_FORMAT(t.TICKET_SALE_END,   '%Y-%m-%d %H:%i') TICKET_SALE_END, " +
                   "         COUNT(tt.TYPE_ID) TYPE_COUNT, " +
                   "         IFNULL(SUM(tt.TYPE_REMAINING), 0) TICKET_REMAINING, " +
                   "         CASE t.TICKET_STATUS WHEN 0 THEN '尚未開始' WHEN 1 THEN '售票中' WHEN 2 THEN '活動結束' WHEN 3 THEN '活動取消' END AS STATUS " +
                   "  FROM holiday_dessert.ticket t " +
                   "  LEFT JOIN holiday_dessert.ticket_type tt ON tt.TICKET_ID = t.TICKET_ID " +
                   "  GROUP BY t.TICKET_ID " +
                   ") AS subquery " +
                   "WHERE (:keyword = '' OR INSTR(TICKET_NAME, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0)",
           countQuery = "SELECT COUNT(*) FROM ( " +
                   "  SELECT TICKET_NAME, " +
                   "         CASE TICKET_STATUS WHEN 0 THEN '尚未開始' WHEN 1 THEN '售票中' WHEN 2 THEN '活動結束' WHEN 3 THEN '活動取消' END AS STATUS " +
                   "  FROM holiday_dessert.ticket " +
                   ") AS subquery " +
                   "WHERE (:keyword = '' OR INSTR(TICKET_NAME, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0)",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);

    // 依票種重新加總活動總票數
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.ticket t " +
                   "SET t.TICKET_QUANTITY = (SELECT IFNULL(SUM(tt.TYPE_QUANTITY), 0) FROM holiday_dessert.ticket_type tt WHERE tt.TICKET_ID = t.TICKET_ID) " +
                   "WHERE t.TICKET_ID = :ticketId",
           nativeQuery = true)
    int refreshQuantity(@Param("ticketId") Integer ticketId);

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

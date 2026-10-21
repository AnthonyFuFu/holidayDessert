package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.TicketOrder;

public interface TicketOrderRepository extends JpaRepository<TicketOrder, Integer> {

    // =============================================
    // backList：後台 DataTables 分頁（ticketId = 0 代表全部活動）
    // =============================================
    @Query(value = "SELECT * FROM ( " +
                   "  SELECT o.TICKET_ORD_ID, t.TICKET_ID, t.TICKET_NAME, tt.TYPE_AREA, tt.TYPE_NAME, m.MEM_NAME, " +
                   "         o.TICKET_ORD_QUANTITY, o.TICKET_ORD_AMOUNT, o.TICKET_ORD_SEATS, o.TICKET_ORD_STATUS, " +
                   "         DATE_FORMAT(o.TICKET_ORD_TIME,   '%Y-%m-%d %H:%i:%s') TICKET_ORD_TIME, " +
                   "         DATE_FORMAT(o.TICKET_ORD_EXPIRE, '%Y-%m-%d %H:%i:%s') TICKET_ORD_EXPIRE, " +
                   "         CASE o.TICKET_ORD_STATUS WHEN 0 THEN '待付款' WHEN 1 THEN '已付款' WHEN 2 THEN '已取消' " +
                   "              WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '逾時取消' END AS STATUS " +
                   "  FROM holiday_dessert.ticket_order o " +
                   "  JOIN holiday_dessert.ticket_type tt ON tt.TYPE_ID = o.TYPE_ID " +
                   "  JOIN holiday_dessert.ticket t ON t.TICKET_ID = tt.TICKET_ID " +
                   "  JOIN holiday_dessert.member m ON m.MEM_ID = o.MEM_ID " +
                   ") AS subquery " +
                   "WHERE (:ticketId = 0 OR TICKET_ID = :ticketId) " +
                   "  AND (:keyword = '' OR INSTR(TICKET_NAME, :keyword) > 0 OR INSTR(TYPE_NAME, :keyword) > 0 " +
                   "       OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0)",
           countQuery = "SELECT COUNT(*) FROM ( " +
                   "  SELECT t.TICKET_ID, t.TICKET_NAME, tt.TYPE_NAME, m.MEM_NAME, " +
                   "         CASE o.TICKET_ORD_STATUS WHEN 0 THEN '待付款' WHEN 1 THEN '已付款' WHEN 2 THEN '已取消' " +
                   "              WHEN 3 THEN '退款中' WHEN 4 THEN '已退款' WHEN 5 THEN '逾時取消' END AS STATUS " +
                   "  FROM holiday_dessert.ticket_order o " +
                   "  JOIN holiday_dessert.ticket_type tt ON tt.TYPE_ID = o.TYPE_ID " +
                   "  JOIN holiday_dessert.ticket t ON t.TICKET_ID = tt.TICKET_ID " +
                   "  JOIN holiday_dessert.member m ON m.MEM_ID = o.MEM_ID " +
                   ") AS subquery " +
                   "WHERE (:ticketId = 0 OR TICKET_ID = :ticketId) " +
                   "  AND (:keyword = '' OR INSTR(TICKET_NAME, :keyword) > 0 OR INSTR(TYPE_NAME, :keyword) > 0 " +
                   "       OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0)",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("ticketId") Integer ticketId, @Param("keyword") String keyword, Pageable pageable);

    // 已售出的座位（待付款 / 已付款），重建 Redis 座位池時排除
    @Query("SELECT o.ticketOrdSeats FROM TicketOrder o " +
           "WHERE o.ticketType.typeId = :typeId AND o.ticketOrdStatus IN (0, 1) AND o.ticketOrdSeats IS NOT NULL")
    List<String> findSoldSeats(@Param("typeId") Integer typeId);

    boolean existsByTicketType_TypeId(Integer typeId);

    // 測試重置用：刪除活動底下所有訂單
    @Modifying
    @Transactional
    @Query(value = "DELETE o FROM holiday_dessert.ticket_order o " +
                   "JOIN holiday_dessert.ticket_type tt ON tt.TYPE_ID = o.TYPE_ID " +
                   "WHERE tt.TICKET_ID = :ticketId",
           nativeQuery = true)
    int deleteByTicketId(@Param("ticketId") Integer ticketId);
}

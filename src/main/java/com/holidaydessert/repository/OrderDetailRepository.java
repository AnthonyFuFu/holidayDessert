package com.holidaydessert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    // =============================================
    // backList：後台 DataTables 分頁 - 訂單明細
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT od.*, PD_NAME, ORD_SUBTOTAL, ORD_TOTAL, ORD_STATUS, " +
               "         DATE_FORMAT(ORD_CREATE, '%Y-%m-%d %H:%i:%s') ORD_CREATE, " +
               "         ORD_RECIPIENT, ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_ADDRESS " +
               "  FROM holiday_dessert.order_detail od " +
               "  LEFT JOIN holiday_dessert.main_order mo ON od.ORD_ID = mo.ORD_ID " +
               "  LEFT JOIN holiday_dessert.product p ON p.PD_ID = od.PD_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(ORD_RECIPIENT, :keyword) > 0 OR INSTR(ORD_ID, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT od.*, PD_NAME, ORD_SUBTOTAL, ORD_TOTAL, ORD_STATUS, " +
               "         DATE_FORMAT(ORD_CREATE, '%Y-%m-%d %H:%i:%s') ORD_CREATE, " +
               "         ORD_RECIPIENT, ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_ADDRESS " +
               "  FROM holiday_dessert.order_detail od " +
               "  LEFT JOIN holiday_dessert.main_order mo ON od.ORD_ID = mo.ORD_ID " +
               "  LEFT JOIN holiday_dessert.product p ON p.PD_ID = od.PD_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(ORD_RECIPIENT, :keyword) > 0 OR INSTR(ORD_ID, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);

    // =============================================
    // frontOrderDetails
    // =============================================
    @Query(value = "SELECT od.*, PD_NAME " +
                   "FROM holiday_dessert.order_detail od " +
                   "LEFT JOIN main_order mo ON od.ORD_ID = mo.ORD_ID " +
                   "LEFT JOIN product p ON p.PD_ID = od.PD_ID " +
                   "WHERE od.ORD_ID = :ordId",
           nativeQuery = true)
    List<Map<String, Object>> frontOrderDetails(@Param("ordId") String ordId);

    // =============================================
    // add → save() 內建，不需要額外方法
    // =============================================
}

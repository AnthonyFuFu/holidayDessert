package com.holidaydessert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.MainOrder;

@Repository
public interface MainOrderRepository extends JpaRepository<MainOrder, String> {

    // =============================================
    // backList：後台 DataTables 分頁 - 訂單
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT ORD_ID, mo.MEM_ID, MEM_CP_ID, MEM_ACCOUNT, MEM_EMAIL, " +
               "         CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS MEM_GENDER, " +
               "         ORD_SUBTOTAL, ORD_TOTAL, ORD_STATUS, DATE_FORMAT(ORD_CREATE, '%Y-%m-%d') ORD_CREATE, " +
               "         ORD_RECIPIENT, ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_ADDRESS, ORD_NOTE, ORD_DELIVERY_FEE, " +
               "         CASE WHEN mo.MEM_CP_ID IS NULL THEN '未使用' " +
               "              ELSE (SELECT CP_NAME FROM holiday_dessert.member_coupon mc " +
               "                    LEFT JOIN holiday_dessert.coupon c ON c.CP_ID = mc.CP_ID " +
               "                    WHERE mc.MEM_CP_ID = mo.MEM_CP_ID LIMIT 1) " +
               "         END AS COUPON_USED " +
               "  FROM holiday_dessert.main_order mo " +
               "  LEFT JOIN holiday_dessert.member m ON m.MEM_ID = mo.MEM_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(MEM_ACCOUNT, :keyword) > 0 OR INSTR(MEM_GENDER, :keyword) > 0 OR INSTR(MEM_EMAIL, :keyword) > 0 OR INSTR(ORD_RECIPIENT, :keyword) > 0 OR INSTR(ORD_RECIPIENT_PHONE, :keyword) > 0 OR INSTR(ORD_ADDRESS, :keyword) > 0 OR INSTR(ORD_NOTE, :keyword) > 0 OR INSTR(COUPON_USED, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT ORD_ID, mo.MEM_ID, MEM_CP_ID, MEM_ACCOUNT, MEM_EMAIL, " +
               "         CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS MEM_GENDER, " +
               "         ORD_SUBTOTAL, ORD_TOTAL, ORD_STATUS, DATE_FORMAT(ORD_CREATE, '%Y-%m-%d') ORD_CREATE, " +
               "         ORD_RECIPIENT, ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_ADDRESS, ORD_NOTE, ORD_DELIVERY_FEE, " +
               "         CASE WHEN mo.MEM_CP_ID IS NULL THEN '未使用' " +
               "              ELSE (SELECT CP_NAME FROM holiday_dessert.member_coupon mc " +
               "                    LEFT JOIN holiday_dessert.coupon c ON c.CP_ID = mc.CP_ID " +
               "                    WHERE mc.MEM_CP_ID = mo.MEM_CP_ID LIMIT 1) " +
               "         END AS COUPON_USED " +
               "  FROM holiday_dessert.main_order mo " +
               "  LEFT JOIN holiday_dessert.member m ON m.MEM_ID = mo.MEM_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(MEM_ACCOUNT, :keyword) > 0 OR INSTR(MEM_GENDER, :keyword) > 0 OR INSTR(MEM_EMAIL, :keyword) > 0 OR INSTR(ORD_RECIPIENT, :keyword) > 0 OR INSTR(ORD_RECIPIENT_PHONE, :keyword) > 0 OR INSTR(ORD_ADDRESS, :keyword) > 0 OR INSTR(ORD_NOTE, :keyword) > 0 OR INSTR(COUPON_USED, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);

    // =============================================
    // add → 使用 JpaRepository 內建 save()，不需要額外方法
    // =============================================
	
    // =============================================
    // getMemOrderList
    // =============================================
    @Query(value = " SELECT MEM_ACCOUNT, " +
                   " CASE MEM_GENDER " +
                   " WHEN 'm' THEN '男' " +
                   " WHEN 'f' THEN '女' " +
                   " END AS MEM_GENDER, " +
                   " MEM_EMAIL, mo.*, " +
                   " CASE WHEN mo.MEM_CP_ID IS NULL THEN '未使用' " +
                   " ELSE (SELECT CP_NAME " +
                   " FROM member_coupon mc " +
                   " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID " +
                   " WHERE mc.MEM_CP_ID = mo.MEM_CP_ID " +
                   " LIMIT 1) " +
                   " END AS COUPON_USED " +
                   " FROM holiday_dessert.main_order mo " +
                   " LEFT JOIN member m ON m.MEM_ID = mo.MEM_ID " +
                   " WHERE mo.MEM_ID = :memId",
           nativeQuery = true)
    List<Map<String, Object>> getMemOrderList(@Param("memId") Integer memId);
    
}

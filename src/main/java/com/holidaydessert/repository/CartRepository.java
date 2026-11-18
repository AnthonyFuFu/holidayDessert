package com.holidaydessert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.Map;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.Cart;
import com.holidaydessert.model.Cart.CartId;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {

    // =============================================
    // backList：後台 DataTables 分頁 - 購物車（排除已刪除商品）
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT c.MEM_ID, c.PD_ID, MEM_NAME, MEM_ACCOUNT, MEM_GENDER, MEM_PHONE, MEM_EMAIL, " +
               "         CART_PD_QUANTITY, PD_NAME, PD_IS_DEL, " +
               "         CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER " +
               "  FROM holiday_dessert.cart c " +
               "  LEFT JOIN holiday_dessert.product p ON p.PD_ID = c.PD_ID " +
               "  LEFT JOIN holiday_dessert.member m ON m.MEM_ID = c.MEM_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(MEM_ACCOUNT, :keyword) > 0 OR INSTR(GENDER, :keyword) > 0 OR INSTR(MEM_PHONE, :keyword) > 0 OR INSTR(MEM_EMAIL, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0) AND PD_IS_DEL = 0 ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT c.MEM_ID, c.PD_ID, MEM_NAME, MEM_ACCOUNT, MEM_GENDER, MEM_PHONE, MEM_EMAIL, " +
               "         CART_PD_QUANTITY, PD_NAME, PD_IS_DEL, " +
               "         CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER " +
               "  FROM holiday_dessert.cart c " +
               "  LEFT JOIN holiday_dessert.product p ON p.PD_ID = c.PD_ID " +
               "  LEFT JOIN holiday_dessert.member m ON m.MEM_ID = c.MEM_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(MEM_NAME, :keyword) > 0 OR INSTR(MEM_ACCOUNT, :keyword) > 0 OR INSTR(GENDER, :keyword) > 0 OR INSTR(MEM_PHONE, :keyword) > 0 OR INSTR(MEM_EMAIL, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0) AND PD_IS_DEL = 0 ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);
	
    @Query(value = "SELECT m.MEM_ID, MEM_NAME, MEM_ACCOUNT, MEM_GENDER, MEM_PHONE, MEM_EMAIL, " +
                   "       CART_PD_QUANTITY, p.* " +
                   "FROM holiday_dessert.cart c " +
                   "LEFT JOIN product p ON p.PD_ID = c.PD_ID " +
                   "LEFT JOIN member m ON m.MEM_ID = c.MEM_ID " +
                   "WHERE c.MEM_ID = :memId " +
                   "AND PD_IS_DEL = 0 " +
                   "AND PD_STATUS = 1",
           nativeQuery = true)
    List<Cart> frontList(@Param("memId") String memId);
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO holiday_dessert.cart (MEM_ID, PD_ID, CART_PD_QUANTITY) " +
                   "VALUES (:memId, :pdId, :quantity)",
           nativeQuery = true)
    void insert(@Param("memId") String memId,
                @Param("pdId") String pdId,
                @Param("quantity") Integer quantity);
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.cart " +
                   "SET CART_PD_QUANTITY = :quantity " +
                   "WHERE MEM_ID = :memId AND PD_ID = :pdId",
           nativeQuery = true)
    void update(@Param("memId") String memId,
                @Param("pdId") String pdId,
                @Param("quantity") Integer quantity);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM holiday_dessert.cart " +
                   "WHERE MEM_ID = :memId AND PD_ID = :pdId",
           nativeQuery = true)
    void delete(@Param("memId") String memId,
                               @Param("pdId") String pdId);
}
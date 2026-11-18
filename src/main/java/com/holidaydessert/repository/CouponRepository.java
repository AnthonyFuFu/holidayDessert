package com.holidaydessert.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {

    // =============================================
    // backList：後台 DataTables 分頁 - 優惠券
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT *, CASE CP_STATUS WHEN '0' THEN '下架' WHEN '1' THEN '上架' END AS STATUS " +
               "  FROM holiday_dessert.coupon " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(CP_NAME, :keyword) > 0 OR INSTR(CP_DISCOUNT, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT *, CASE CP_STATUS WHEN '0' THEN '下架' WHEN '1' THEN '上架' END AS STATUS " +
               "  FROM holiday_dessert.coupon " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(CP_NAME, :keyword) > 0 OR INSTR(CP_DISCOUNT, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);
}

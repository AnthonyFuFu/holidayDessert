package com.holidaydessert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, String> {

    // =============================================
    // backList：後台 DataTables 分頁 - 促銷活動
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT PM_ID, PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_REGULARLY, PM_STATUS, " +
               "         DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, " +
               "         DATE_FORMAT(PM_END,   '%Y-%m-%d %H:%i:%s') PM_END, " +
               "         CASE PM_STATUS WHEN '0' THEN '下架' WHEN '1' THEN '上架' END AS STATUS, " +
               "         CASE PM_REGULARLY WHEN '0' THEN '非例行' WHEN '1' THEN '例行' END AS REGULARLY " +
               "  FROM holiday_dessert.promotion " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(PM_NAME, :keyword) > 0 OR INSTR(PM_DESCRIPTION, :keyword) > 0 OR INSTR(PM_DISCOUNT, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0 OR INSTR(REGULARLY, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT PM_ID, PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_REGULARLY, PM_STATUS, " +
               "         DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, " +
               "         DATE_FORMAT(PM_END,   '%Y-%m-%d %H:%i:%s') PM_END, " +
               "         CASE PM_STATUS WHEN '0' THEN '下架' WHEN '1' THEN '上架' END AS STATUS, " +
               "         CASE PM_REGULARLY WHEN '0' THEN '非例行' WHEN '1' THEN '例行' END AS REGULARLY " +
               "  FROM holiday_dessert.promotion " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(PM_NAME, :keyword) > 0 OR INSTR(PM_DESCRIPTION, :keyword) > 0 OR INSTR(PM_DISCOUNT, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0 OR INSTR(REGULARLY, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);

    // =============================================
    // nearestStartList
    // =============================================
    @Query(value = "SELECT PM_ID, PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, " +
                   "       DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, " +
                   "       DATE_FORMAT(PM_END,   '%Y-%m-%d %H:%i:%s') PM_END " +
                   "FROM holiday_dessert.promotion " +
                   "WHERE PM_END >= NOW() " +
                   "AND PM_STATUS = 1 " +
                   "ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PM_START))",
           nativeQuery = true)
    List<Map<String, Object>> nearestStartList();

    // =============================================
    // getData：直接回傳 Promotion Entity
    // 原本手動 Map 轉換的邏輯，JPA 自動處理
    // =============================================
    @Query(value = "SELECT PM_ID, PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_REGULARLY, PM_STATUS, " +
                   "       DATE_FORMAT(PM_START, '%Y-%m-%d') PM_START, " +
                   "       DATE_FORMAT(PM_END,   '%Y-%m-%d') PM_END " +
                   "FROM holiday_dessert.promotion " +
                   "WHERE PM_ID = :pmId",
           nativeQuery = true)
    Optional<Promotion> getData(@Param("pmId") String pmId);
}
package com.holidaydessert.repository;

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
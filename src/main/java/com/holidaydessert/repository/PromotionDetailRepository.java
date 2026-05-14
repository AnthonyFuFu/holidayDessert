package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, String> {

    // =============================================
    // frontList
    // =============================================
    @Query(value = "SELECT PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_STATUS, " +
                   "       DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, " +
                   "       DATE_FORMAT(PM_END,   '%Y-%m-%d %H:%i:%s') PM_END, " +
                   "       PMD_ID, pmd.PD_ID, pmd.PM_ID, PMD_PD_DISCOUNT_PRICE, " +
                   "       DATE_FORMAT(PMD_START, '%Y-%m-%d %H:%i:%s') PMD_START, " +
                   "       DATE_FORMAT(PMD_END,   '%Y-%m-%d %H:%i:%s') PMD_END, " +
                   "       PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_STATUS, PD_IS_DEL " +
                   "FROM holiday_dessert.promotion_detail pmd " +
                   "LEFT JOIN product p ON pmd.PD_ID = p.PD_ID " +
                   "LEFT JOIN promotion pm ON pmd.PM_ID = pm.PM_ID " +
                   "WHERE pmd.PM_ID = :pmId " +
                   "AND PD_IS_DEL = 0",
           nativeQuery = true)
    List<Map<String, Object>> frontList(@Param("pmId") String pmId);
}
package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, String> {

    // =============================================
    // findDataById：後台單筆查詢（修改頁使用）- 促銷明細（日期格式化為 yyyy-MM-dd，供日期選擇器使用）
    // 需要格式化的欄位明確列出，新增欄位時記得一併加入 SELECT
    // =============================================
    @Query(value = "SELECT PMD_ID, PD_ID, PM_ID, PMD_PD_DISCOUNT_PRICE, " +
                   "       DATE_FORMAT(PMD_START, '%Y-%m-%d') PMD_START, " +
                   "       DATE_FORMAT(PMD_END,   '%Y-%m-%d') PMD_END " +
                   "FROM holiday_dessert.promotion_detail " +
                   "WHERE PMD_ID = :pmdId ",
           nativeQuery = true)
    Optional<PromotionDetail> findDataById(@Param("pmdId") String pmdId);

    // =============================================
    // backList：後台 DataTables 分頁 - 促銷明細（排除已刪除商品）
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT PMD_ID, pmd.PD_ID, pmd.PM_ID, PMD_PD_DISCOUNT_PRICE, " +
               "         DATE_FORMAT(PMD_START, '%Y-%m-%d %H:%i:%s') PMD_START, " +
               "         DATE_FORMAT(PMD_END,   '%Y-%m-%d %H:%i:%s') PMD_END, " +
               "         PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_REGULARLY, PM_STATUS, " +
               "         DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, " +
               "         DATE_FORMAT(PM_END,   '%Y-%m-%d %H:%i:%s') PM_END, " +
               "         PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_STATUS, PD_IS_DEL " +
               "  FROM holiday_dessert.promotion_detail pmd " +
               "  LEFT JOIN holiday_dessert.product p ON pmd.PD_ID = p.PD_ID " +
               "  LEFT JOIN holiday_dessert.promotion pm ON pmd.PM_ID = pm.PM_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(PM_NAME, :keyword) > 0 OR INSTR(PM_DESCRIPTION, :keyword) > 0 OR INSTR(PM_DISCOUNT, :keyword) > 0 OR INSTR(PMD_PD_DISCOUNT_PRICE, :keyword) > 0 OR INSTR(PMD_START, :keyword) > 0 OR INSTR(PMD_END, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0 OR INSTR(PD_PRICE, :keyword) > 0) AND PD_IS_DEL = 0 ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT PMD_ID, pmd.PD_ID, pmd.PM_ID, PMD_PD_DISCOUNT_PRICE, " +
               "         DATE_FORMAT(PMD_START, '%Y-%m-%d %H:%i:%s') PMD_START, " +
               "         DATE_FORMAT(PMD_END,   '%Y-%m-%d %H:%i:%s') PMD_END, " +
               "         PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_REGULARLY, PM_STATUS, " +
               "         DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, " +
               "         DATE_FORMAT(PM_END,   '%Y-%m-%d %H:%i:%s') PM_END, " +
               "         PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_STATUS, PD_IS_DEL " +
               "  FROM holiday_dessert.promotion_detail pmd " +
               "  LEFT JOIN holiday_dessert.product p ON pmd.PD_ID = p.PD_ID " +
               "  LEFT JOIN holiday_dessert.promotion pm ON pmd.PM_ID = pm.PM_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(PM_NAME, :keyword) > 0 OR INSTR(PM_DESCRIPTION, :keyword) > 0 OR INSTR(PM_DISCOUNT, :keyword) > 0 OR INSTR(PMD_PD_DISCOUNT_PRICE, :keyword) > 0 OR INSTR(PMD_START, :keyword) > 0 OR INSTR(PMD_END, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0 OR INSTR(PD_PRICE, :keyword) > 0) AND PD_IS_DEL = 0 ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);

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
package com.holidaydessert.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    // =============================================
    // issuePromotionList：後台 DataTables 分頁 - 發放促銷：目前不在促銷期間的上架商品（每個商品取最近一筆促銷）
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT sub.PD_ID, sub.PM_ID, sub.PDC_NAME, sub.PD_NAME, sub.PD_DESCRIPTION, sub.PD_PRICE, sub.PMD_START, sub.PMD_END " +
               "  FROM (SELECT p.PD_ID, pmd.PM_ID, PDC_NAME, PD_NAME, PD_DESCRIPTION, PD_PRICE, " +
               "               DATE_FORMAT(PMD_START, '%Y-%m-%d') AS PMD_START, " +
               "               DATE_FORMAT(PMD_END,   '%Y-%m-%d') AS PMD_END, " +
               "               ROW_NUMBER() OVER (PARTITION BY p.PD_ID ORDER BY PMD_END DESC) AS rn " +
               "        FROM holiday_dessert.product p " +
               "        LEFT JOIN holiday_dessert.product_collection pc ON p.PDC_ID = pc.PDC_ID " +
               "        LEFT JOIN holiday_dessert.promotion_detail pmd ON p.PD_ID = pmd.PD_ID " +
               "        LEFT JOIN holiday_dessert.promotion pm ON pmd.PM_ID = pm.PM_ID " +
               "        WHERE p.PD_STATUS = 1 AND p.PD_IS_DEL = 0) AS sub " +
               "  WHERE sub.rn = 1 " +
               "    AND (CURDATE() < PMD_START OR CURDATE() > PMD_END OR PMD_START IS NULL OR PMD_END IS NULL) " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(PDC_NAME, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT sub.PD_ID, sub.PM_ID, sub.PDC_NAME, sub.PD_NAME, sub.PD_DESCRIPTION, sub.PD_PRICE, sub.PMD_START, sub.PMD_END " +
               "  FROM (SELECT p.PD_ID, pmd.PM_ID, PDC_NAME, PD_NAME, PD_DESCRIPTION, PD_PRICE, " +
               "               DATE_FORMAT(PMD_START, '%Y-%m-%d') AS PMD_START, " +
               "               DATE_FORMAT(PMD_END,   '%Y-%m-%d') AS PMD_END, " +
               "               ROW_NUMBER() OVER (PARTITION BY p.PD_ID ORDER BY PMD_END DESC) AS rn " +
               "        FROM holiday_dessert.product p " +
               "        LEFT JOIN holiday_dessert.product_collection pc ON p.PDC_ID = pc.PDC_ID " +
               "        LEFT JOIN holiday_dessert.promotion_detail pmd ON p.PD_ID = pmd.PD_ID " +
               "        LEFT JOIN holiday_dessert.promotion pm ON pmd.PM_ID = pm.PM_ID " +
               "        WHERE p.PD_STATUS = 1 AND p.PD_IS_DEL = 0) AS sub " +
               "  WHERE sub.rn = 1 " +
               "    AND (CURDATE() < PMD_START OR CURDATE() > PMD_END OR PMD_START IS NULL OR PMD_END IS NULL) " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(PDC_NAME, :keyword) > 0 OR INSTR(PD_NAME, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> issuePromotionList(@Param("keyword") String keyword, Pageable pageable);

    // =============================================
    // 共用 SQL 片段（抽取共同部分）
    // =============================================
    String BASE_SELECT =
            "SELECT p.PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, " +
            "       PD_STATUS, DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, " +
            "       CASE PD_STATUS " +
            "           WHEN '0' THEN '下架' " +
            "           WHEN '1' THEN '上架' " +
            "       END AS STATUS, PD_PIC_ID, PD_PIC_SORT, PD_PICTURE, PD_IMAGE " +
            "FROM holiday_dessert.product p " +
            "LEFT JOIN product_pic ppic ON p.PD_ID = ppic.PD_ID " +
            "WHERE PD_STATUS = 1 " +
            "AND PD_IS_DEL = 0 " +
            "AND (p.PD_ID, PD_PIC_SORT) IN ( " +
            "    SELECT PD_ID, MIN(PD_PIC_SORT) " +
            "    FROM product_pic " +
            "    GROUP BY PD_ID) ";

    // =============================================
    // getMainProductList（依更新時間排序）
    // =============================================
    @Query(value = BASE_SELECT +
                   "ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PD_UPDATE_TIME))",
           nativeQuery = true)
    List<Map<String, Object>> getMainProductList();

    // =============================================
    // getNewArrivalList
    // =============================================
    @Query(value = BASE_SELECT +
                   "ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PD_UPDATE_TIME))",
           nativeQuery = true)
    List<Map<String, Object>> getNewArrivalList();

    // =============================================
    // frontTypeList（依分類 + 更新時間排序）
    // =============================================
    @Query(value = BASE_SELECT +
                   "AND PDC_ID = :pdcId " +
                   "ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PD_UPDATE_TIME))",
           nativeQuery = true)
    List<Map<String, Object>> frontTypeList(@Param("pdcId") String pdcId);

    // =============================================
    // frontRandTypeList（依分類 + 隨機排序）
    // =============================================
    @Query(value = BASE_SELECT +
                   "AND PDC_ID = :pdcId " +
                   "ORDER BY RAND()",
           nativeQuery = true)
    List<Map<String, Object>> frontRandTypeList(@Param("pdcId") String pdcId);
    
}

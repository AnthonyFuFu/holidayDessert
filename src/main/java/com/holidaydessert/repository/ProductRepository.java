package com.holidaydessert.repository;

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

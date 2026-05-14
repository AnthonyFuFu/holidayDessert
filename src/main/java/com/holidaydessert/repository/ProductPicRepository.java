package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.ProductPic;

@Repository
public interface ProductPicRepository extends JpaRepository<ProductPic, String> {

    // =============================================
    // frontRandList：帶 LIMIT（Pageable 處理動態 LIMIT）
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.product_pic " +
                   "WHERE PD_ID = :pdId " +
                   "ORDER BY RAND()",
           nativeQuery = true)
    List<Map<String, Object>> frontRandList(@Param("pdId") String pdId, Pageable pageable);

    // =============================================
    // frontRandList：不帶 LIMIT（查全部）
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.product_pic " +
                   "WHERE PD_ID = :pdId " +
                   "ORDER BY RAND()",
           nativeQuery = true)
    List<Map<String, Object>> frontRandListAll(@Param("pdId") String pdId);
}
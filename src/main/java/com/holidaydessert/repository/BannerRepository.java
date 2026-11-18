package com.holidaydessert.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String> {

    // =============================================
    // findDataById：後台單筆查詢（修改頁使用）- 輪播圖
    // SELECT * 直接對應 Entity，之後新增欄位只要在 Entity 加上 @Column 即可
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.banner " +
                   "WHERE BAN_ID = :banId ",
           nativeQuery = true)
    Optional<Banner> findDataById(@Param("banId") String banId);

    // MySQL 專屬語法 RAND()，使用 nativeQuery = true
    @Query(value = "SELECT * FROM holiday_dessert.banner " +
                   "WHERE NEWS_ID = :newsId " +
                   "ORDER BY RAND() LIMIT 3",
           nativeQuery = true)
    List<Banner> findFrontRandList(@Param("newsId") String newsId);
    
}

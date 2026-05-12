package com.holidaydessert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, String> {

    // MySQL 專屬語法 RAND()，使用 nativeQuery = true
    @Query(value = "SELECT * FROM holiday_dessert.banner " +
                   "WHERE NEWS_ID = :newsId " +
                   "ORDER BY RAND() LIMIT 3",
           nativeQuery = true)
    List<Banner> findFrontRandList(@Param("newsId") String newsId);
    
}

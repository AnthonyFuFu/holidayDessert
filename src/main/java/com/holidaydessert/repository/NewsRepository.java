package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {

    // =============================================
    // frontList：帶分頁（Pageable 處理動態 LIMIT）
    // =============================================
    @Query(value = "SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, " +
                   "       DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, " +
                   "       DATE_FORMAT(NEWS_END,   '%Y-%m-%d') NEWS_END, " +
                   "       DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d') NEWS_CREATE " +
                   "FROM holiday_dessert.news " +
                   "WHERE NEWS_STATUS = 1 " +
                   "ORDER BY NEWS_CREATE DESC",
           countQuery = "SELECT COUNT(*) FROM holiday_dessert.news WHERE NEWS_STATUS = 1",
           nativeQuery = true)
    List<Map<String, Object>> frontList(Pageable pageable);

    // =============================================
    // frontList：不帶分頁（全部查詢）
    // =============================================
    @Query(value = "SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, " +
                   "       DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, " +
                   "       DATE_FORMAT(NEWS_END,   '%Y-%m-%d') NEWS_END, " +
                   "       DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d') NEWS_CREATE " +
                   "FROM holiday_dessert.news " +
                   "WHERE NEWS_STATUS = 1 " +
                   "ORDER BY NEWS_CREATE DESC",
           nativeQuery = true)
    List<Map<String, Object>> frontListAll();

    // =============================================
    // frontRandList：隨機取 3 筆
    // =============================================
    @Query(value = "SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, " +
                   "       DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, " +
                   "       DATE_FORMAT(NEWS_END,   '%Y-%m-%d') NEWS_END, " +
                   "       DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d') NEWS_CREATE " +
                   "FROM holiday_dessert.news " +
                   "WHERE NEWS_STATUS = 1 " +
                   "ORDER BY RAND() LIMIT 3",
           nativeQuery = true)
    List<Map<String, Object>> frontRandList();
    
}

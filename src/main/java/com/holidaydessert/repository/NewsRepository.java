package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {

    // =============================================
    // backList：後台 DataTables 分頁（回傳欄位與 NewsDao.list 相同）
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value = "SELECT * FROM ( " +
                   "  SELECT NEWS_ID, n.PM_ID, " +
                   "         CASE WHEN n.PM_ID IS NULL OR n.PM_ID = 0 THEN '公告' ELSE PM_NAME END AS PM_NAME, " +
                   "         NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, " +
                   "         DATE_FORMAT(NEWS_START,  '%Y-%m-%d %H:%i:%s') NEWS_START, " +
                   "         DATE_FORMAT(NEWS_END,    '%Y-%m-%d %H:%i:%s') NEWS_END, " +
                   "         DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d %H:%i:%s') NEWS_CREATE, " +
                   "         CASE NEWS_STATUS WHEN '0' THEN '下架' WHEN '1' THEN '上架' END AS STATUS " +
                   "  FROM holiday_dessert.news n " +
                   "  LEFT JOIN holiday_dessert.promotion p ON p.PM_ID = n.PM_ID " +
                   ") AS subquery " +
                   "WHERE (:keyword = '' " +
                   "       OR INSTR(NEWS_NAME, :keyword) > 0 OR INSTR(NEWS_CONTENT, :keyword) > 0 " +
                   "       OR INSTR(PM_NAME, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0)",
           countQuery = "SELECT COUNT(*) FROM ( " +
                   "  SELECT NEWS_NAME, NEWS_CONTENT, " +
                   "         CASE WHEN n.PM_ID IS NULL OR n.PM_ID = 0 THEN '公告' ELSE PM_NAME END AS PM_NAME, " +
                   "         CASE NEWS_STATUS WHEN '0' THEN '下架' WHEN '1' THEN '上架' END AS STATUS " +
                   "  FROM holiday_dessert.news n " +
                   "  LEFT JOIN holiday_dessert.promotion p ON p.PM_ID = n.PM_ID " +
                   ") AS subquery " +
                   "WHERE (:keyword = '' " +
                   "       OR INSTR(NEWS_NAME, :keyword) > 0 OR INSTR(NEWS_CONTENT, :keyword) > 0 " +
                   "       OR INSTR(PM_NAME, :keyword) > 0 OR INSTR(STATUS, :keyword) > 0)",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);

    // =============================================
    // findDataById：後台單筆查詢（修改頁使用）
    // =============================================
    @Query(value = "SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, " +
                   "       DATE_FORMAT(NEWS_START,  '%Y-%m-%d') NEWS_START, " +
                   "       DATE_FORMAT(NEWS_END,    '%Y-%m-%d') NEWS_END, " +
                   "       DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d %H:%i:%s') NEWS_CREATE " +
                   "FROM holiday_dessert.news " +
                   "WHERE NEWS_ID = :newsId",
           nativeQuery = true)
    Optional<News> findDataById(@Param("newsId") String newsId);

    // =============================================
    // findOnlineList：上架中的最新消息（Banner 下拉選單使用）
    // =============================================
    @Query(value = "SELECT NEWS_ID, NEWS_NAME FROM holiday_dessert.news WHERE NEWS_STATUS = 1",
           nativeQuery = true)
    List<Map<String, Object>> findOnlineList();

    // =============================================
    // offlineById：軟刪除（NEWS_STATUS 改為 0 下架）
    // =============================================
    @Transactional
    @Modifying
    @Query(value = "UPDATE holiday_dessert.news SET NEWS_STATUS = 0 WHERE NEWS_ID = :newsId",
           nativeQuery = true)
    int offlineById(@Param("newsId") String newsId);

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
    Page<News> frontList(Pageable pageable);

    @Query(value = "SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, " +
            	   "       DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, " +
            	   "       DATE_FORMAT(NEWS_END,   '%Y-%m-%d') NEWS_END, " +
            	   "       DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d') NEWS_CREATE " +
            	   "FROM holiday_dessert.news " +
            	   "WHERE NEWS_STATUS = 1 " +
            	   "AND (LOWER(NEWS_NAME) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(NEWS_CONTENT) LIKE LOWER(CONCAT('%', :keyword, '%')))",
			countQuery = "SELECT COUNT(*) FROM holiday_dessert.news WHERE NEWS_STATUS = 1 AND (LOWER(NEWS_NAME) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(NEWS_CONTENT) LIKE LOWER(CONCAT('%', :keyword, '%')))", nativeQuery = true)
	Page<News> frontListByKeyword(@Param("keyword") String keyword, Pageable pageable);

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

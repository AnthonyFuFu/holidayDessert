package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.News;

@Repository
public class NewsDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
	public void add(News news) {

		String sql = " INSERT INTO holiday_dessert.news "
				   + " (PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, NEWS_START, NEWS_END, NEWS_CREATE) "
				   + " VALUES(?, ?, ?, ?, CONCAT( ?, ' 00:00:00'), CONCAT( ?, ' 23:59:59'), NOW()) ";
		
		jdbcTemplate.update(sql, news.getPmId(), news.getNewsName(), news.getNewsContent(),
				news.getNewsStatus(), news.getNewsStart(), news.getNewsEnd());
	}

	public void update(News news) {

		String sql = " UPDATE holiday_dessert.news "
				   + " SET PM_ID = ?, NEWS_NAME = ?, NEWS_CONTENT = ?, NEWS_STATUS = ?, "
				   + " NEWS_START = CONCAT( ?, ' 00:00:00'), NEWS_END = CONCAT( ?, ' 23:59:59') "
				   + " WHERE NEWS_ID = ? ";
		
		jdbcTemplate.update(sql, news.getPmId(), news.getNewsName(), news.getNewsContent(),
				news.getNewsStatus(), news.getNewsStart(), news.getNewsEnd(), news.getNewsId());
	}

	public List<Map<String, Object>> getListForBanner() {

		String sql = " SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, "
				   + " DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, "
				   + " DATE_FORMAT(NEWS_END, '%Y-%m-%d') NEWS_END, "
				   + " DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d %H:%i:%s') NEWS_CREATE "
				   + " FROM holiday_dessert.news ";
		
		// 回傳可修改的 Map（Controller 會再 put bannerList）
		return jdbcTemplate.queryForList(sql);
	}
	
}

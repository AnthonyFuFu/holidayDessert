package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.NewsDao;
import com.holidaydessert.model.News;

@Repository
public class NewsDaoImpl implements NewsDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(News news) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT "		   
				   + " NEWS_ID, n.PM_ID, "
				   + " CASE WHEN n.PM_ID IS NULL THEN '公告' "
				   + " WHEN n.PM_ID = 0 THEN '公告' "
				   + " ELSE PM_NAME "
				   + " END AS PM_NAME, "
				   + " NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, "
				   + " DATE_FORMAT(NEWS_START, '%Y-%m-%d %H:%i:%s') NEWS_START, "
				   + " DATE_FORMAT(NEWS_END, '%Y-%m-%d %H:%i:%s') NEWS_END, "
				   + " DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d %H:%i:%s') NEWS_CREATE, "
				   + " CASE NEWS_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.news n "
				   + " LEFT JOIN promotion p ON p.PM_ID = n.PM_ID "
				   + ") AS subquery ";
		
		if (news.getSearchText() != null && news.getSearchText().length() > 0) {
			String[] searchText = news.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(NEWS_NAME, ?) > 0"
					+  " OR INSTR(NEWS_CONTENT, ?) > 0 "
					+  " OR INSTR(PM_NAME, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if (news.getStart() != null && !"".equals(news.getStart())) {
			sql += " LIMIT " + news.getStart() + "," + news.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(News news) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( SELECT n.*,p.PM_NAME, "
				   + " CASE NEWS_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.news n "
				   + " LEFT JOIN promotion p ON p.PM_ID = n.PM_ID "
				   + ") AS subquery ";
		
		if (news.getSearchText() != null && news.getSearchText().length() > 0) {
			String[] searchText = news.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(NEWS_NAME, ?) > 0"
					+  " OR INSTR(NEWS_CONTENT, ?) > 0 "
					+  " OR INSTR(PM_NAME, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(News news) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.news "
				   + " (PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, NEWS_START, NEWS_END, NEWS_CREATE) "
				   + " VALUES(?, ?, ?, ?, CONCAT( ?, ' 00:00:00'), CONCAT( ?, ' 23:59:59'), NOW()) ";
		
		args.add(news.getPmId());
		args.add(news.getNewsName());
		args.add(news.getNewsContent());
		args.add(news.getNewsStatus());
		args.add(news.getNewsStart());
		args.add(news.getNewsEnd());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(News news) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.news "
				   + " SET PM_ID = ?, NEWS_NAME = ?, NEWS_CONTENT = ?, NEWS_STATUS = ?, "
				   + " NEWS_START = CONCAT( ?, ' 00:00:00'), NEWS_END = CONCAT( ?, ' 23:59:59') "
				   + " WHERE NEWS_ID = ? ";
		
		args.add(news.getPmId());
		args.add(news.getNewsName());
		args.add(news.getNewsContent());
		args.add(news.getNewsStatus());
		args.add(news.getNewsStart());
		args.add(news.getNewsEnd());
		args.add(news.getNewsId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void delete(News news) {

		String sql = " UPDATE holiday_dessert.news "
				   + " SET NEWS_STATUS = 0 "
				   + " WHERE NEWS_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { news.getNewsId() });
		
	}

	@Override
	public News getData(News news) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, "
				   + " DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, "
				   + " DATE_FORMAT(NEWS_END, '%Y-%m-%d') NEWS_END, "
				   + " DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d %H:%i:%s') NEWS_CREATE "
				   + " FROM holiday_dessert.news "
				   + " WHERE NEWS_ID = ? ";
		
		args.add(news.getNewsId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		News item = new News();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        String newsId = String.valueOf(resultMap.get("NEWS_ID"));
	        String pmId = String.valueOf(resultMap.get("PM_ID"));
	        String newsName = String.valueOf(resultMap.get("NEWS_NAME"));
	        String newsContent = String.valueOf(resultMap.get("NEWS_CONTENT"));
	        String newsStatus = String.valueOf(resultMap.get("NEWS_STATUS"));
	        String newsStart = String.valueOf(resultMap.get("NEWS_START"));
	        String newsEnd = String.valueOf(resultMap.get("NEWS_END"));
	        String newsCreate = String.valueOf(resultMap.get("NEWS_CREATE"));
	        
	        item.setNewsId(newsId);
	        item.setPmId(pmId);
	        item.setNewsName(newsName);
	        item.setNewsContent(newsContent);
	        item.setNewsStatus(newsStatus);
	        item.setNewsStart(newsStart);
	        item.setNewsEnd(newsEnd);
	        item.setNewsCreate(newsCreate);
	    }
		return item == null ? null : item;
	}

	@Override
	public List<Map<String, Object>> getList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.news "
				   + " WHERE NEWS_STATUS = 1 ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public List<Map<String, Object>> getListForBanner() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, "
				   + " DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, "
				   + " DATE_FORMAT(NEWS_END, '%Y-%m-%d') NEWS_END, "
				   + " DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d %H:%i:%s') NEWS_CREATE "
				   + " FROM holiday_dessert.news ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public List<Map<String, Object>> frontList(News news) {

		String sql = " SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, "
				   + " DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, "
				   + " DATE_FORMAT(NEWS_END, '%Y-%m-%d') NEWS_END, "
				   + " DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d') NEWS_CREATE "
				   + " FROM holiday_dessert.news "
				   + " WHERE NEWS_STATUS = 1 "
				   + " ORDER BY NEWS_CREATE DESC ";

		if (news.getStart() != null && !"".equals(news.getStart())) {
			sql += " LIMIT " + news.getStart() + "," + news.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<Map<String, Object>> frontRandList(News news) {

		String sql = " SELECT NEWS_ID, PM_ID, NEWS_NAME, NEWS_CONTENT, NEWS_STATUS, "
				   + " DATE_FORMAT(NEWS_START, '%Y-%m-%d') NEWS_START, "
				   + " DATE_FORMAT(NEWS_END, '%Y-%m-%d') NEWS_END, "
				   + " DATE_FORMAT(NEWS_CREATE, '%Y-%m-%d') NEWS_CREATE "
				   + " FROM holiday_dessert.news "
				   + " WHERE NEWS_STATUS = 1 "
				   + " ORDER BY RAND() LIMIT 3 ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

}

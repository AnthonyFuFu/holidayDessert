package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.BannerDao;
import com.holidayDessert.model.Banner;

@Repository
public class BannerDaoImpl implements BannerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Banner banner) {

		String sql = " SELECT * FROM holiday_dessert.banner ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public int getCount(Banner banner) {
		
//		List<Object> args = new ArrayList<Object>();
//
//		String sql = " SELECT COUNT(*) AS COUNT FROM (SELECT ID, IMAGE_URL, TITLE, LINK, CLICK_RATE,  "
//				   + " DATE_FORMAT(BEGIN_DATE, '%Y-%m-%d') BEGIN_DATE, DATE_FORMAT(END_DATE, '%Y-%m-%d') END_DATE, "
//				   + " (CASE IS_SHOW WHEN '0' THEN '否' WHEN '1' THEN '是' ELSE '' END) AS IS_SHOW, "
//				   + " SORT, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, BANNER_TYPE "
//				   + " FROM forecast.banner  "
//				   + " )A ";
//
//		if(!"".equals(banner.getSearch_text()) && banner.getSearch_text() != null) {
//			sql += " WHERE INSTR(TITLE, ?) > 0 ";
//			sql += " OR INSTR(LINK, ?) > 0 ";
//			sql += " OR INSTR(CLICK_RATE, ?) > 0 ";
//			sql += " OR INSTR(BEGIN_DATE, ?) > 0 ";
//			sql += " OR INSTR(END_DATE, ?) > 0 ";
//			sql += " OR INSTR(IS_SHOW, ?) > 0 ";
//			sql += " OR INSTR(SORT, ?) > 0 ";
//			
//			args.add(banner.getSearch_text());
//			args.add(banner.getSearch_text());
//			args.add(banner.getSearch_text());
//			args.add(banner.getSearch_text());
//			args.add(banner.getSearch_text());
//			args.add(banner.getSearch_text());
//			args.add(banner.getSearch_text());
//			
//		}
//		
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
//		
//		if(list!=null && list.size()>0) {
//			return Integer.valueOf(list.get(0).get("COUNT").toString());
//		} else {
			return 0;
//		}
		
	}

	@Override
	public void add(Banner banner) {
//		String sql = " INSERT INTO forecast.banner "
//				   + " (ID, IMAGE_URL, TITLE, LINK, BEGIN_DATE, END_DATE, IS_SHOW, SORT, BANNER_TYPE, "
//				   + " CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME)  "
//				   + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW()) ";
//		
//		jdbcTemplate.update(sql, new Object[] {banner.getId(), banner.getImage_url(),
//				banner.getTitle(), banner.getLink(), banner.getBegin_date(),
//				banner.getEnd_date() ,banner.getIs_show(), banner.getSort(),
//				banner.getBanner_type(), banner.getCreate_by(), banner.getUpdate_by()
//				});
	}

	@Override
	public void update(Banner banner) {
//		String sql = " UPDATE forecast.banner "
//				   + " SET IMAGE_URL = ?, TITLE = ?, LINK = ?, BEGIN_DATE = ?, END_DATE = ?, "
//				   + " IS_SHOW = ?, SORT = ?, BANNER_TYPE= ?, UPDATE_BY = ?, UPDATE_TIME = NOW() "
//				   + " WHERE ID = ? ";
//		
//		jdbcTemplate.update(sql, new Object[] { banner.getImage_url(), banner.getTitle(),
//				banner.getLink(), banner.getBegin_date(), banner.getEnd_date(),
//				banner.getIs_show(), banner.getSort(), banner.getBanner_type(),
//				banner.getUpdate_by(), banner.getId()});
	}

	@Override
	public void delete(Banner banner) {
//		String sql = " DELETE FROM forecast.banner "
//				   + " WHERE ID = ? ";
//		
//		jdbcTemplate.update(sql, new Object[] { 
//				id });
	}

	@Override
	public List<Map<String, Object>> frontRandList(Banner banner) {
//
//		List<Object> args = new ArrayList<Object>();
//		
//		String sql = " SELECT * FROM forecast.banner WHERE ID >= (SELECT ID FROM    "
//				   + " forecast.banner ORDER BY ID LIMIT 0 , 1) AND IS_SHOW='1' "
////				   + " AND NOW() BETWEEN BEGIN_DATE AND END_DATE AND BANNER_TYPE = ? "
//				   + " AND STR_TO_DATE(BEGIN_DATE, '%Y-%m-%d') <= NOW() AND STR_TO_DATE(DATE_ADD(END_DATE, INTERVAL 1 DAY) , '%Y-%m-%d') > NOW() "
//				   + " AND BANNER_TYPE = ? ";
//		
//		if("A".equals(banner.getBanner_type())) { 
//			sql += " ORDER BY RAND( ) LIMIT 5 "; 
//		}else { 
//			sql += " ORDER BY RAND( ) LIMIT 4 "; 
//		}
//		
//		args.add(banner.getBanner_type());
//		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
//		
//		if(list!=null && list.size()>0) {
//			return list;
//		} else {
			return null;
//		}
	}

}
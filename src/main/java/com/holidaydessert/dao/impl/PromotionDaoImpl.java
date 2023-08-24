package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.PromotionDao;
import com.holidaydessert.model.Promotion;

@Repository
public class PromotionDaoImpl implements PromotionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Promotion promotion) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT PM_ID, PM_NAME, PM_DESCRIPTION, "
				   + " PM_DISCOUNT, PM_REGULARLY, PM_STATUS,"
				   + " DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, "
				   + " DATE_FORMAT(PM_END, '%Y-%m-%d %H:%i:%s') PM_END, "
				   + " CASE PM_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, "
				   + " CASE PM_REGULARLY "
				   + " WHEN '0' THEN '非例行' "
				   + " WHEN '1' THEN '例行' "
				   + " END AS REGULARLY "
				   + " FROM holiday_dessert.promotion "
				   + " ) AS subquery ";
		
		if (promotion.getSearchText() != null && promotion.getSearchText().length() > 0) {
			String[] searchText = promotion.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PM_NAME, ?) > 0"
					+  " OR INSTR(PM_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PM_DISCOUNT, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " OR INSTR(REGULARLY, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if (promotion.getStart() != null && !"".equals(promotion.getStart())) {
			sql += " LIMIT " + promotion.getStart() + "," + promotion.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(Promotion promotion) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( SELECT *, "
				   + " CASE PM_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, "
				   + " CASE PM_REGULARLY "
				   + " WHEN '0' THEN '非例行' "
				   + " WHEN '1' THEN '例行' "
				   + " END AS REGULARLY "
				   + " FROM holiday_dessert.promotion "
				   + " ) AS subquery ";
		
		if (promotion.getSearchText() != null && promotion.getSearchText().length() > 0) {
			String[] searchText = promotion.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PM_NAME, ?) > 0"
					+  " OR INSTR(PM_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PM_DISCOUNT, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " OR INSTR(REGULARLY, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(Promotion promotion) {

		String sql = " INSERT INTO holiday_dessert.promotion "
				   + " (PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_REGULARLY, PM_STATUS, PM_START, PM_END) "
				   + " VALUES(?, ?, ?, ?, ?, CONCAT( ?, ' 00:00:00'), CONCAT( ?, ' 23:59:59')) ";

		jdbcTemplate.update(sql, new Object[] { promotion.getPmName(), promotion.getPmDescription(), promotion.getPmDiscount(), promotion.getPmStatus() });
		
	}

	@Override
	public void update(Promotion promotion) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.promotion "
				   + " SET PM_NAME = ?, PM_DESCRIPTION = ?, PM_DISCOUNT = ?, PM_REGULARLY = ? , "
				   + " PM_STATUS = ?, PM_START = CONCAT( ?, ' 00:00:00') , PM_END = CONCAT( ?, ' 23:59:59')  "
				   + " WHERE PM_ID = ? ";
		
		args.add(promotion.getPmName());
		args.add(promotion.getPmDescription());
		args.add(promotion.getPmDiscount());
		args.add(promotion.getPmRegularly());
		args.add(promotion.getPmStatus());
		args.add(promotion.getPmStart());
		args.add(promotion.getPmEnd());
		args.add(promotion.getPmId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void takeDown(Promotion promotion) {

		String sql = " UPDATE holiday_dessert.promotion "
				   + " SET PM_STATUS = 0 "
				   + " WHERE PM_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { promotion.getPmId() });
		
	}

	@Override
	public List<Map<String, Object>> getList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.promotion "
				   + " WHERE PM_STATUS = 1 "
				   + " AND CURDATE() BETWEEN PM_START AND PM_END ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public List<Map<String, Object>> nearestStartList(Promotion promotion) {
		
		String sql = " SELECT PM_ID, PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, "
				   + " DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, "
				   + " DATE_FORMAT(PM_END, '%Y-%m-%d %H:%i:%s') PM_END "
				   + " FROM holiday_dessert.promotion "
				   + " WHERE PM_END >= NOW() "
				   + " AND PM_STATUS = 1 "
				   + " ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PM_START)) ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

}

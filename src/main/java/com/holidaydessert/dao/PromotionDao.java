package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Promotion;

@Repository
public class PromotionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
	public void add(Promotion promotion) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.promotion "
				   + " (PM_NAME, PM_DESCRIPTION, PM_DISCOUNT, PM_REGULARLY, PM_STATUS, PM_START, PM_END) "
				   + " VALUES(?, ?, ?, ?, ?, CONCAT( ?, ' 00:00:00'), CONCAT( ?, ' 23:59:59')) ";
		
		args.add(promotion.getPmName());
		args.add(promotion.getPmDescription());
		args.add(promotion.getPmDiscount());
		args.add(promotion.getPmRegularly());
		args.add(promotion.getPmStatus());
		args.add(promotion.getPmStart());
		args.add(promotion.getPmEnd());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

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

	public void takeDown(Promotion promotion) {

		String sql = " UPDATE holiday_dessert.promotion "
				   + " SET PM_STATUS = 0 "
				   + " WHERE PM_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { promotion.getPmId() });
		
	}

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
	
}

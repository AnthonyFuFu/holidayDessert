package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Coupon;

@Repository
public class CouponDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// =============================================
	// back
	// =============================================
	public void add(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.coupon "
				   + " (CP_NAME, CP_DISCOUNT, CP_STATUS, CP_PICTURE, CP_IMAGE) "
				   + " VALUES(?, ?, ?, ?, ?) ";

		args.add(coupon.getCpName());
		args.add(coupon.getCpDiscount());
		args.add(coupon.getCpStatus());
		args.add(coupon.getCpPicture());
		args.add(coupon.getCpImage());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	public void update(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.coupon "
				   + " SET CP_NAME = ?, CP_DISCOUNT = ?, CP_STATUS = ?, CP_PICTURE = ?, CP_IMAGE = ? "
				   + " WHERE CP_ID = ? ";
		
		args.add(coupon.getCpName());
		args.add(coupon.getCpDiscount());
		args.add(coupon.getCpStatus());
		args.add(coupon.getCpPicture());
		args.add(coupon.getCpImage());
		args.add(coupon.getCpId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	public void takeDown(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.coupon "
				   + " SET CP_STATUS = ? "
				   + " WHERE CP_ID = ? ";

		args.add(coupon.getCpStatus());
		args.add(coupon.getCpId());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	public List<Map<String, Object>> getList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.coupon "
				   + " WHERE CP_STATUS = '1' ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}
	
}

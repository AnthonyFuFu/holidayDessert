package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.CouponDao;
import com.holidayDessert.model.Coupon;

@Repository
public class CouponDaoImpl implements CouponDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(Coupon coupon) {
		
		String sql = " SELECT * FROM holiday_dessert.coupon ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public int getCount(Coupon coupon) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(Coupon coupon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Coupon coupon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Coupon coupon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> frontList(Coupon coupon) {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.PromotionDetailDao;
import com.holidaydessert.model.PromotionDetail;

@Repository
public class PromotionDetailDaoImpl implements PromotionDetailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(PromotionDetail promotionDetail) {

		String sql = " SELECT * FROM holiday_dessert.promotion_detail ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(PromotionDetail promotionDetail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(PromotionDetail promotionDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PromotionDetail promotionDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PromotionDetail promotionDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> frontNewList(PromotionDetail promotionDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> frontTypeList(PromotionDetail promotionDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(PromotionDetail promotionDetail) {
		// TODO Auto-generated method stub
		return null;
	}

}

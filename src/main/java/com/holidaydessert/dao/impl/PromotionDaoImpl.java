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

		String sql = " SELECT * FROM holiday_dessert.promotion ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(Promotion promotion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(Promotion promotion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Promotion promotion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Promotion promotion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> frontNewList(Promotion promotion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> frontTypeList(Promotion promotion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(Promotion promotion) {
		// TODO Auto-generated method stub
		return null;
	}

}

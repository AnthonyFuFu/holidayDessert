package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.MainOrderDao;
import com.holidayDessert.model.MainOrder;

@Repository
public class MainOrderDaoImpl implements MainOrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(MainOrder mainOrder) {

		String sql = " SELECT * FROM holiday_dessert.main_order ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(MainOrder mainOrder) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(MainOrder mainOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(MainOrder mainOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(MainOrder mainOrder) {
		// TODO Auto-generated method stub
		
	}

}

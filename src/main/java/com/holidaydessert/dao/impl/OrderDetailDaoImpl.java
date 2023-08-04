package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.OrderDetailDao;
import com.holidaydessert.model.OrderDetail;

@Repository
public class OrderDetailDaoImpl implements OrderDetailDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(OrderDetail orderDetail) {

		String sql = " SELECT * FROM holiday_dessert.order_detail ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(OrderDetail orderDetail) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(OrderDetail orderDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(OrderDetail orderDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> frontList(OrderDetail orderDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(OrderDetail orderDetail) {
		// TODO Auto-generated method stub
		
	}

}

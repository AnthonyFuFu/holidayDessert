package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ProductDao;
import com.holidaydessert.model.Product;

@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Product product) {

		String sql = " SELECT * FROM holiday_dessert.product ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(Product product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> frontNewList(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> frontTypeList(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

}

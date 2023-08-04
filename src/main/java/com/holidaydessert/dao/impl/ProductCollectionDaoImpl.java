package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ProductCollectionDao;
import com.holidaydessert.model.ProductCollection;

@Repository
public class ProductCollectionDaoImpl implements ProductCollectionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(ProductCollection productCollection) {

		String sql = " SELECT * FROM holiday_dessert.product_collection ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(ProductCollection productCollection) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(ProductCollection productCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ProductCollection productCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ProductCollection productCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> frontList(ProductCollection productCollection) {
		// TODO Auto-generated method stub
		return null;
	}

}

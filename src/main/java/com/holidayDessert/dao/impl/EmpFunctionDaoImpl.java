package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.EmpFunctionDao;
import com.holidayDessert.model.EmpFunction;

@Repository
public class EmpFunctionDaoImpl implements EmpFunctionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(EmpFunction empFunction) {
		
		String sql = " SELECT * FROM holiday_dessert.emp_function ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public void add(EmpFunction empFunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(EmpFunction empFunction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(EmpFunction empFunction) {
		// TODO Auto-generated method stub
		
	}

}

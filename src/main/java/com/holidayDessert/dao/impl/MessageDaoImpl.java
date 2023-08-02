package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.MessageDao;
import com.holidayDessert.model.Message;

@Repository
public class MessageDaoImpl implements MessageDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Message message) {

		String sql = " SELECT * FROM holiday_dessert.message ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public void edit(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Message message) {
		// TODO Auto-generated method stub
		
	}

}

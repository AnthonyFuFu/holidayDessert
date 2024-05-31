package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.TokenDao;

@Repository
public class TokenDaoImpl implements TokenDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getToken(String memEmail,String memPassword) {
		
		List<Object> args = new ArrayList<>();
		String sql = " SELECT * FROM holiday_dessert.member "
				   + " WHERE MEM_EMAIL = ? AND MEM_PASSWORD = ? ";
		
		args.add(memEmail);
		args.add(memPassword);

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

}

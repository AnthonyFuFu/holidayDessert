package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.MessageDao;
import com.holidaydessert.model.Message;

@Repository
public class MessageDaoImpl implements MessageDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> getMessageByEmp(Message message) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT msg.*, MEM_NAME, EMP_NAME FROM holiday_dessert.message msg "
				   + " LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID "
				   + " LEFT JOIN employee e ON e.EMP_ID = msg.EMP_ID "
				   + " WHERE m.MEM_ID = ? "
				   + " AND msg.ROOM_ID = ? "
				   + " AND msg.MSG_CONTENT IS NOT NULL "
				   + " ORDER BY msg.MSG_TIME ASC ";

		args.add(message.getMemId());
		args.add(message.getRoomId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}
	
}

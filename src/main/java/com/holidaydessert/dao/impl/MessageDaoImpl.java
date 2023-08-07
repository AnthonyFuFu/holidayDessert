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
	public List<Map<String, Object>> getMessageByEmpId(Message message) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT msg.*, MEM_NAME, EMP_NAME FROM holiday_dessert.message msg "
				   + " LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID "
				   + " LEFT JOIN employee e ON e.EMP_ID = msg.EMP_ID "
				   + " WHERE e.EMP_ID = ? ";
		
		args.add(message.getEmpId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<Map<String, Object>> getMessageByMemId(Message message) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT msg.*, MEM_NAME, EMP_NAME FROM holiday_dessert.message msg "
				   + " LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID "
				   + " LEFT JOIN employee e ON e.EMP_ID = msg.EMP_ID "
				   + " WHERE m.MEM_ID = ? ";
		
		args.add(message.getMemId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public void saveMessage(Message message) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.product_pic "
				   + " (EMP_ID, MEM_ID, MSG_CONTENT, MSG_TIME, MSG_DIRECTION, MSG_PIC) "
				   + " VALUES(?, ?, ?, ?, ?, ?) ";
		
		args.add(message.getEmpId());
		args.add(message.getMemId());
		args.add(message.getMsgContent());
		args.add(message.getMsgTime());
		args.add(message.getMsgDirection());
		args.add(message.getMsgPic());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void edit(Message message) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.message "
				   + " SET MSG_CONTENT = ?, MSG_TIME = ? "
				   + " WHERE MSG_ID = ? ";
		
		args.add(message.getMsgContent());
		args.add(message.getMsgTime());
		args.add(message.getMsgId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}
	
	@Override
	public void delete(Message message) {

		List<Object> args = new ArrayList<>();
		
		String sql = " DELETE FROM holiday_dessert.message "
				   + " WHERE MSG_ID = ? ";
		
		args.add(message.getMsgId());
		jdbcTemplate.update(sql, args.toArray());
		
	}

}

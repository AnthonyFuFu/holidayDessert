package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ChatRoomDao;

@Repository
public class ChatRoomDaoImpl implements ChatRoomDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> getAllChatRoom() {
		
		String sql = " SELECT room.*,msg.MEM_ID,msg.EMP_ID,m.MEM_NAME,COUNT(msg.MEM_ID) AS MSG_COUNT FROM holiday_dessert.chat_room room "
				   + " LEFT JOIN message msg ON room.ROOM_ID = msg.ROOM_ID "
				   + " LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID "
				   + " GROUP BY MEM_ID ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public Long addChatRoom(String roomUrl) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.chat_room "
				   + " (ROOM_URL, ROOM_STATUS, ROOM_UPDATE_STATUS, ROOM_LAST_UPDATE) "
				   + " VALUES(?, ?, ?, NOW()) ";
		
		args.add(roomUrl);
		args.add(1);
		args.add(0);
		
		jdbcTemplate.update(sql, args.toArray());
		return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()",Long.class);
	}
	
	@Override
	public List<Map<String, Object>> getChatRoomByMessage(String memId) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT ROOM_ID FROM holiday_dessert.message msg "
				   + " WHERE MEM_ID = ? "
				   + " GROUP BY ROOM_ID ";
		
		args.add(memId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<Map<String, Object>> getChatRoom(String chatRoomId) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT chat.*,msg.EMP_ID,msg.MEM_ID,(SELECT e.EMP_NAME FROM employee e WHERE e.EMP_ID = msg.EMP_ID LIMIT 1) AS EMP_NAME "
				   + " FROM holiday_dessert.chat_room chat "
				   + " LEFT JOIN message msg ON chat.ROOM_ID = msg.ROOM_ID "
				   + " WHERE chat.ROOM_ID = ? "
				   + " GROUP BY chat.ROOM_ID ";
		
		args.add(chatRoomId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}
	
	@Override
	public List<Map<String, Object>> getServiceStaff(String memId) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT msg.*, MEM_NAME, EMP_NAME FROM holiday_dessert.message msg "
				   + " LEFT JOIN member m ON m.MEM_ID = msg.MEM_ID "
				   + " LEFT JOIN employee e ON e.EMP_ID = msg.EMP_ID "
				   + " WHERE m.MEM_ID = ? ";
		
		args.add(memId);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}
	
}

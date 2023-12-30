package com.holidaydessert.dao.impl;

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

}

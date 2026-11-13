package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> getAllChatRoom() {
		
		// 以 chat_room 為主，一間聊天室只會有一筆；訊息數用子查詢計算，不再被 GROUP BY 拆成多筆
		String sql = " SELECT room.*, m.MEM_NAME, e.EMP_NAME, "
				   + "        (SELECT COUNT(*) FROM message msg WHERE msg.ROOM_ID = room.ROOM_ID) AS MSG_COUNT "
				   + " FROM holiday_dessert.chat_room room "
				   + " LEFT JOIN member m ON m.MEM_ID = room.MEM_ID "
				   + " LEFT JOIN employee e ON e.EMP_ID = room.EMP_ID "
				   + " ORDER BY room.ROOM_ID ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}
	
}

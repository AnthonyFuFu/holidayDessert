package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

public interface ChatRoomDao {

	// back
	public List<Map<String, Object>> getAllChatRoom();

	// front
	public Long addChatRoom(String roomUrl);
	public List<Map<String, Object>> getChatRoomByMessage(String memId);
	public List<Map<String, Object>> getChatRoom(String chatRoomId);
	public List<Map<String, Object>> getServiceStaff(String memId);
}

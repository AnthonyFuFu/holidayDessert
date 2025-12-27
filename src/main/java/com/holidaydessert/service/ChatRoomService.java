package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ApiReturnObject;

public interface ChatRoomService {

	// back
	public List<Map<String, Object>> getAllChatRoom();

	// front
	public ApiReturnObject getChatRoom(String memId);
	public ApiReturnObject getServiceStaff(String memId);
}

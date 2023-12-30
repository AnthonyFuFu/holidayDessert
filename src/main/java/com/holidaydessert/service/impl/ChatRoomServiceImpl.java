package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ChatRoomDao;
import com.holidaydessert.service.ChatRoomService;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

	@Autowired
	private ChatRoomDao chatRoomDao;
	
	@Override
	public List<Map<String, Object>> getAllChatRoom() {
		return chatRoomDao.getAllChatRoom();
	}
	
}

package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.MessageDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Message;
import com.holidaydessert.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	@Override
	public ApiReturnObject getMessageByEmp(Message message) {
		List<Map<String, Object>> getMessage = messageDao.getMessageByEmp(message);
		return new ApiReturnObject(200, "取得客服對會員對話紀錄", getMessage);
	}

	@Override
	public ApiReturnObject getMessageByMem(Message message) {
		List<Map<String, Object>> getMessage = messageDao.getMessageByMem(message);
		return new ApiReturnObject(200, "取得會員對客服對話紀錄", getMessage);
	}

	@Override
	public void saveMessage(Message message) {
		messageDao.saveMessage(message);
	}

	@Override
	public void edit(Message message) {
		messageDao.edit(message);
	}

	@Override
	public void delete(Message message) {
		messageDao.delete(message);
	}

}

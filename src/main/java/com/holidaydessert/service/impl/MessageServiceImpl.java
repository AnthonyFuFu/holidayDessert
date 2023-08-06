package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.MessageDao;
import com.holidaydessert.model.Message;
import com.holidaydessert.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	@Override
	public List<Map<String, Object>> getMessageByEmpId(Message message) {
		return messageDao.getMessageByEmpId(message);
	}

	@Override
	public List<Map<String, Object>> getMessageByMemId(Message message) {
		return messageDao.getMessageByMemId(message);
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

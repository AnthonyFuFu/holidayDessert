package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.MessageDao;
import com.holidayDessert.model.Message;
import com.holidayDessert.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public List<Map<String, Object>> list(Message message) {
		return messageDao.list(message);
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

package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.MessageDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Message;
import com.holidaydessert.repository.MessageRepository;
import com.holidaydessert.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Override
	public ApiReturnObject getMessageByEmp(Message message) {
		List<Map<String, Object>> getMessage = messageDao.getMessageByEmp(message);
		return ApiReturnObject.success("取得客服對會員對話紀錄", getMessage);
	}

	// =============================================
	// getMessageByMem
	// =============================================
	@Override
	public ApiReturnObject getMessageByMem(Message message) {
		List<Map<String, Object>> getMessage = messageRepository.getMessageByMem(message.getMemId());
		return ApiReturnObject.success("取得會員對客服對話紀錄", getMessage);
	}
	
	// =============================================
	// saveMessage
	// =============================================
	@Override
	@Transactional
	public void saveMessage(Message message) {
	    messageRepository.save(message);
	}

	// =============================================
	// edit
	// =============================================
	@Override
	@Transactional
	public void edit(Message message) {
	    messageRepository.edit(
	        message.getMsgId(),
	        message.getMsgContent(),
	        message.getMsgTime()
	    );
	}

	// =============================================
	// delete
	// =============================================
	@Override
	@Transactional
	public void delete(Message message) {
	    messageRepository.deleteByMsgId(message.getMsgId());
	}
	
}

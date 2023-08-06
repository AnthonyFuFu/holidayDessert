package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Message;

public interface MessageService {
	
	// back
	public List<Map<String, Object>> getMessageByEmpId(Message message);
	
	// front
	public List<Map<String, Object>> getMessageByMemId(Message message);
	public void saveMessage(Message message);
	public void edit(Message message);
	public void delete(Message message);
	
}

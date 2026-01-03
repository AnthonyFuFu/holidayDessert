package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Message;

public interface MessageDao {

	// back
	public List<Map<String, Object>> getMessageByEmp(Message message);
		
	// front
	public List<Map<String, Object>> getMessageByMem(Message message);
	public void saveMessage(Message message);
	public void edit(Message message);
	public void delete(Message message);
	
}

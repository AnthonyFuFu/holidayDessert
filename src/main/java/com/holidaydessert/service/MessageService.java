package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Message;

public interface MessageService {
	
	// front
	public List<Map<String, Object>> list(Message message);
	public void edit(Message message);
	public void delete(Message message);

}

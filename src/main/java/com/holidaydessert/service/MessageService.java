package com.holidaydessert.service;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Message;

public interface MessageService {
	
	// back
	public ApiReturnObject getMessageByEmp(Message message);
	
	// front
	public ApiReturnObject getMessageByMem(Message message);
	public void saveMessage(Message message);
	public void edit(Message message);
	public void delete(Message message);
	
}

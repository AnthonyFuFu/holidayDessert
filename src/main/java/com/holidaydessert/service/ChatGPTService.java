package com.holidaydessert.service;

import com.holidaydessert.model.ApiReturnObject;

public interface ChatGPTService {
	
	public ApiReturnObject getChat(String prompt);
	
}

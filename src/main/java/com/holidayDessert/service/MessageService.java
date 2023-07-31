package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Message;

public interface MessageService {

	public List<Map<String, Object>> list(Message message);

}

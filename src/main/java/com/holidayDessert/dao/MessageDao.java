package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Message;

public interface MessageDao {

	public List<Map<String, Object>> list(Message message);

}

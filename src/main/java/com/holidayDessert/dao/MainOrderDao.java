package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.MainOrder;

public interface MainOrderDao {
	
	public List<Map<String, Object>> list(MainOrder mainOrder);
	
}

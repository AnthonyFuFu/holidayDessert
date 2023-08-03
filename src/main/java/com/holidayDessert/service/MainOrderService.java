package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.MainOrder;

public interface MainOrderService {

	// back
	public List<Map<String, Object>> list(MainOrder mainOrder);
	public int getCount(MainOrder mainOrder);
	public void update(MainOrder mainOrder);
	
	// front
	public void add(MainOrder mainOrder);
	
}

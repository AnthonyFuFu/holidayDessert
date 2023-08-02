package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.MainOrder;

public interface MainOrderDao {
	
	// back
	public List<Map<String, Object>> list(MainOrder mainOrder);
	public int getCount(MainOrder mainOrder);
	public void update(MainOrder mainOrder);
	public void delete(MainOrder mainOrder);
	
	// front
	public void add(MainOrder mainOrder);
	
}

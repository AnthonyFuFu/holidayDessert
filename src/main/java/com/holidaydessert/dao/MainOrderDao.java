package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.MainOrder;

public interface MainOrderDao {
	
	// back
	public List<Map<String, Object>> list(MainOrder mainOrder);
	public int getCount(MainOrder mainOrder);
	public void update(MainOrder mainOrder);
	
	// front
	public void add(MainOrder mainOrder);
	public List<Map<String, Object>> getMemOrderList(MainOrder mainOrder);
	
}

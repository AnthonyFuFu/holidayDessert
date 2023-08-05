package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.OrderDetail;

public interface OrderDetailService {
	
	// back
	public List<Map<String, Object>> list(OrderDetail orderDetail);
	public int getCount(OrderDetail orderDetail);
			
	// front
	public List<Map<String, Object>> frontOrderDetails(OrderDetail orderDetail);
	public void add(OrderDetail orderDetail);
	
}

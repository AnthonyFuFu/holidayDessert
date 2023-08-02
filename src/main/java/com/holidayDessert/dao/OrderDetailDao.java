package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.OrderDetail;

public interface OrderDetailDao {

	// back
	public List<Map<String, Object>> list(OrderDetail orderDetail);
	public int getCount(OrderDetail orderDetail);
	public void update(OrderDetail orderDetail);
	public void delete(OrderDetail orderDetail);
					
	// front
	public List<Map<String, Object>> frontList(OrderDetail orderDetail);
	public void add(OrderDetail orderDetail);
	
}

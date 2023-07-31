package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.OrderDetail;

public interface OrderDetailDao {

	public List<Map<String, Object>> list(OrderDetail orderDetail);

}

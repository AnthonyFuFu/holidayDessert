package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.OrderDetail;

public interface OrderDetailService {

	public List<Map<String, Object>> list(OrderDetail orderDetail);

}

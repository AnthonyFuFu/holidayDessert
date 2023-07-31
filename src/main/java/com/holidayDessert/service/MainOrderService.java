package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.MainOrder;

public interface MainOrderService {

	public List<Map<String, Object>> list(MainOrder mainOrder);

}

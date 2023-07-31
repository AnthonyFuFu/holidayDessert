package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Cart;

public interface CartDao {
	
	public List<Map<String, Object>> list(Cart cart);
	
}

package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Cart;

public interface CartService {
	
	public List<Map<String, Object>> list(Cart cart);

}

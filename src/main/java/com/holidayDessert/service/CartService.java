package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Cart;

public interface CartService {
	
	// back
	public List<Map<String, Object>> list(Cart cart);
	public int getCount(Cart cart);
	public void add(Cart cart);
	public void update(Cart cart);
	public void delete(Cart cart);
	
}

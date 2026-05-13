package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Cart;

public interface CartService {
	
	// back
	public List<Map<String, Object>> list(Cart cart);
	public Integer getCount(Cart cart);
	
	// front
	public List<Cart> frontList(String memId);
	public void insert(Cart cart);
	public void update(Cart cart);
	public void delete(Cart cart);
	
}

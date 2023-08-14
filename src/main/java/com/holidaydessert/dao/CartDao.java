package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Cart;

public interface CartDao {

	// back
	public List<Map<String, Object>> list(Cart cart);
	public Integer getCount(Cart cart);
	
	// front
	public List<Map<String, Object>> frontList(Cart cart);
	public void add(Cart cart);
	public void update(Cart cart);
	public void delete(Cart cart);
	
}

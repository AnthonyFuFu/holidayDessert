package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Cart;

public interface CartDao {

	// back
	public List<Map<String, Object>> list(Cart cart);
	public Integer getCount(Cart cart);
	
}

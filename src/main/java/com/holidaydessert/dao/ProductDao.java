package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Product;

public interface ProductDao {

	// back
	public List<Map<String, Object>> list(Product product);
	public int getCount(Product product);
	public void add(Product product);
	public void update(Product product);
	public void delete(Product product);
	
	// front
	public List<Map<String, Object>> frontNewList(Product product);
	public List<Map<String, Object>> frontTypeList(Product product);
	public List<Map<String, Object>> frontRandTypeList(Product product);
	
}
package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Product;

public interface ProductDao {

	public List<Map<String, Object>> list(Product product);

}

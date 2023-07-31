package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Product;

public interface ProductService {

	public List<Map<String, Object>> list(Product product);

}

package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.ProductCollection;

public interface ProductCollectionService {

	public List<Map<String, Object>> list(ProductCollection productCollection);

}

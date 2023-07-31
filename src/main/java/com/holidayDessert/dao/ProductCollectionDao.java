package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.ProductCollection;

public interface ProductCollectionDao {

	public List<Map<String, Object>> list(ProductCollection productCollection);

}

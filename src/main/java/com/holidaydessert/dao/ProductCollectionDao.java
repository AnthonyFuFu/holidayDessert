package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ProductCollection;

public interface ProductCollectionDao {

	// back
	public List<Map<String, Object>> list(ProductCollection productCollection);
	public int getCount(ProductCollection productCollection);
	public void add(ProductCollection productCollection);
	public void update(ProductCollection productCollection);
	public void takeDown(ProductCollection productCollection);
	public ProductCollection getData(ProductCollection productCollection);
	public List<Map<String, Object>> getList();
	
	// front
	public List<Map<String, Object>> getAllPdcList();
	public List<Map<String, Object>> getPdByPdcName(String pdcName);
	
}

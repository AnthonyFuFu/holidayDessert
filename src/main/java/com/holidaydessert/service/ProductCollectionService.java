package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.ProductCollection;

public interface ProductCollectionService {
	
	// back
	public List<Map<String, Object>> list(ProductCollection productCollection);
	public int getCount(ProductCollection productCollection);
	public void add(ProductCollection productCollection);
	public void update(ProductCollection productCollection);
	public void takeDown(ProductCollection productCollection);
	public ProductCollection getData(ProductCollection productCollection);
	public List<Map<String, Object>> getList();
	
	// front
	public ApiReturnObject getAllPdcList();
	public ApiReturnObject getPdByPdcName(String pdcName);
	
}

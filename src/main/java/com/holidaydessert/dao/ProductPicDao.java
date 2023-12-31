package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ProductPic;

public interface ProductPicDao {

	// back
	public List<Map<String, Object>> list(ProductPic productPic);
	public void add(ProductPic productPic);
	public void update(ProductPic productPic);
	public void delete(ProductPic productPic);
	public ProductPic getData(ProductPic productPic);
	
	// front
	public List<Map<String, Object>> frontRandList(ProductPic productPic);
	
}

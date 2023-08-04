package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ProductPic;

public interface ProductPicDao {

	public List<Map<String, Object>> list(ProductPic productPic);

}

package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.ProductPic;

public interface ProductPicDao {

	public List<Map<String, Object>> list(ProductPic productPic);

}

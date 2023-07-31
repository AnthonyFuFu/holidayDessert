package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.ProductPicDao;
import com.holidayDessert.model.ProductPic;
import com.holidayDessert.service.ProductPicService;

@Service
public class ProductPicServiceImpl implements ProductPicService {

	@Autowired
	private ProductPicDao productPicDao;

	@Override
	public List<Map<String, Object>> list(ProductPic productPic) {
		return productPicDao.list(productPic);
	}

}

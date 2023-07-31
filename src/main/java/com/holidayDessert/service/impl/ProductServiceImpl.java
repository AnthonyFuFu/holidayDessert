package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.ProductDao;
import com.holidayDessert.model.Product;
import com.holidayDessert.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public List<Map<String, Object>> list(Product product) {
		return productDao.list(product);
	}

}

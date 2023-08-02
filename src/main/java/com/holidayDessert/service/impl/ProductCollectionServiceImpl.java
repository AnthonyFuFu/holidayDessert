package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.ProductCollectionDao;
import com.holidayDessert.model.ProductCollection;
import com.holidayDessert.service.ProductCollectionService;

@Service
public class ProductCollectionServiceImpl implements ProductCollectionService {

	@Autowired
	private ProductCollectionDao productCollectionDao;

	@Override
	public List<Map<String, Object>> list(ProductCollection productCollection) {
		return productCollectionDao.list(productCollection);
	}

	@Override
	public int getCount(ProductCollection productCollection) {
		return productCollectionDao.getCount(productCollection);
	}

	@Override
	public void add(ProductCollection productCollection) {
		productCollectionDao.add(productCollection);
	}

	@Override
	public void update(ProductCollection productCollection) {
		productCollectionDao.update(productCollection);
	}

	@Override
	public void delete(ProductCollection productCollection) {
		productCollectionDao.delete(productCollection);
	}

	@Override
	public List<Map<String, Object>> frontList(ProductCollection productCollection) {
		return productCollectionDao.frontList(productCollection);
	}

}

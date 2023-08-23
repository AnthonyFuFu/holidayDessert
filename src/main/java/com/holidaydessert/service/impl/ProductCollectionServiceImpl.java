package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductCollectionDao;
import com.holidaydessert.model.ProductCollection;
import com.holidaydessert.service.ProductCollectionService;

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
	public void takeDown(ProductCollection productCollection) {
		productCollectionDao.takeDown(productCollection);
	}

	@Override
	public ProductCollection getData(ProductCollection productCollection) {
		return productCollectionDao.getData(productCollection);
	}

	@Override
	public List<Map<String, Object>> getList() {
		return productCollectionDao.getList();
	}

	@Override
	public List<Map<String, Object>> frontList(ProductCollection productCollection) {
		return productCollectionDao.frontList(productCollection);
	}

}

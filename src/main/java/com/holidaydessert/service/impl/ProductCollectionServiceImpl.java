package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductCollectionDao;
import com.holidaydessert.model.ApiReturnObject;
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
	public ApiReturnObject getAllPdcList() {
		
		List<Map<String, Object>> list = productCollectionDao.getAllPdcList();
		if(list == null) {
			return new ApiReturnObject(200, "查無商品分類清單", null);
		}
		
		return new ApiReturnObject(200, "取得商品分類清單成功", list);
		
	}

	@Override
	public ApiReturnObject getPdByPdcName(String pdcName) {
		
		List<Map<String, Object>> list = productCollectionDao.getPdByPdcName(pdcName);
		if(list == null) {
			return new ApiReturnObject(200, "查無商品分類", null);
		}
		
		return new ApiReturnObject(200, "取得商品分類成功", list);
		
	}

}

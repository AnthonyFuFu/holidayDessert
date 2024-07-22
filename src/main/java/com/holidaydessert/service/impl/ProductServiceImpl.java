package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Product;
import com.holidaydessert.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public List<Map<String, Object>> list(Product product) {
		return productDao.list(product);
	}

	@Override
	public int getCount(Product product) {
		return productDao.getCount(product);
	}

	@Override
	public void add(Product product) {
		productDao.add(product);
	}

	@Override
	public void update(Product product) {
		productDao.update(product);
	}

	@Override
	public void delete(Product product) {
		productDao.delete(product);
	}

	@Override
	public Product getData(Product product) {
		return productDao.getData(product);
	}
	
	@Override
	public List<Map<String, Object>> getList() {
		return productDao.getList();
	}
	
	@Override
	public List<Map<String, Object>> getPicList() {
		return productDao.getPicList();
	}

	@Override
	public List<Map<String, Object>> issuePromotionList(Product product) {
		return productDao.issuePromotionList(product);
	}
	
	@Override
	public List<Map<String, Object>> issueOneProductList() {
		return productDao.issueOneProductList();
	}
	
	@Override
	public int getIssuePromotionCount(Product product) {
		return productDao.getIssuePromotionCount(product);
	}

	@Override
	public ApiReturnObject getMainProductList() {
		
		List<Map<String, Object>> newArrivalList = productDao.getMainProductList();
		
		if(newArrivalList == null) {
			return new ApiReturnObject(200, "查無主要產品清單", null);
		}
		
		return new ApiReturnObject(200, "取得主要產品清單成功", newArrivalList);
	}
	
	@Override
	public ApiReturnObject getNewArrivalList() {
		
		List<Map<String, Object>> newArrivalList = productDao.getNewArrivalList();
		
		if(newArrivalList == null) {
			return new ApiReturnObject(200, "查無新品上市清單", null);
		}
		
		return new ApiReturnObject(200, "取得新品上市清單成功", newArrivalList);
	}
	
	@Override
	public List<Map<String, Object>> frontTypeList(Product product) {
		return productDao.frontTypeList(product);
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(Product product) {
		return productDao.frontRandTypeList(product);
	}

}

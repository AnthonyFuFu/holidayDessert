package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductCollectionDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.ProductCollection;
import com.holidaydessert.repository.ProductCollectionRepository;

@Service
public class ProductCollectionService {

	@Autowired
	private ProductCollectionDao productCollectionDao;
	
	@Autowired
	private ProductCollectionRepository productCollectionRepository;
	
	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(ProductCollection productCollection) {
		return productCollectionDao.list(productCollection);
	}
	
	public int getCount(ProductCollection productCollection) {
		return productCollectionDao.getCount(productCollection);
	}
	
	public void add(ProductCollection productCollection) {
		productCollectionDao.add(productCollection);
	}
	
	public void update(ProductCollection productCollection) {
		productCollectionDao.update(productCollection);
	}
	
	public void takeDown(ProductCollection productCollection) {
		productCollectionDao.takeDown(productCollection);
	}
	
	public ProductCollection getData(ProductCollection productCollection) {
		return productCollectionDao.getData(productCollection);
	}
	
	public List<Map<String, Object>> getList() {
		return productCollectionDao.getList();
	}

	// =============================================
	// front
	// =============================================
	public ApiReturnObject getAllPdcList() {
	    List<Map<String, Object>> list = productCollectionRepository.getAllPdcList();
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無商品分類清單", null);
	    }
	    return ApiReturnObject.success("取得商品分類清單成功", list);
	}
	
	public ApiReturnObject getPdByPdcName(String pdcName) {
	    List<Map<String, Object>> list = productCollectionRepository.getPdByPdcName(pdcName);
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無商品分類", null);
	    }
	    return ApiReturnObject.success("取得商品分類成功", list);
	}
	
}

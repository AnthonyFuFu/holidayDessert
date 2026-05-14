package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductCollectionDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.ProductCollection;
import com.holidaydessert.repository.ProductCollectionRepository;
import com.holidaydessert.service.ProductCollectionService;

@Service
public class ProductCollectionServiceImpl implements ProductCollectionService {

	@Autowired
	private ProductCollectionDao productCollectionDao;
	
	@Autowired
	private ProductCollectionRepository productCollectionRepository;
	
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
	
	// =============================================
	// getAllPdcList
	// =============================================
	@Override
	public ApiReturnObject getAllPdcList() {
	    List<Map<String, Object>> list = productCollectionRepository.getAllPdcList();
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無商品分類清單", null);
	    }
	    return ApiReturnObject.success("取得商品分類清單成功", list);
	}

	// =============================================
	// getPdByPdcName
	// =============================================
	@Override
	public ApiReturnObject getPdByPdcName(String pdcName) {
	    List<Map<String, Object>> list = productCollectionRepository.getPdByPdcName(pdcName);
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無商品分類", null);
	    }
	    return ApiReturnObject.success("取得商品分類成功", list);
	}

}

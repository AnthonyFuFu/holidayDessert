package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Product;
import com.holidaydessert.repository.ProductRepository;
import com.holidaydessert.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductRepository productRepository;
	
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

	// =============================================
	// getMainProductList
	// =============================================
	@Override
	public ApiReturnObject getMainProductList() {
	    List<Map<String, Object>> list = productRepository.getMainProductList();
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無主要產品清單", null);
	    }
	    return ApiReturnObject.success("取得主要產品清單成功", list);
	}

	// =============================================
	// getNewArrivalList
	// =============================================
	@Override
	public ApiReturnObject getNewArrivalList() {
	    List<Map<String, Object>> list = productRepository.getNewArrivalList();
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無新品上市清單", null);
	    }
	    return ApiReturnObject.success("取得新品上市清單成功", list);
	}

	// =============================================
	// frontTypeList
	// =============================================
	@Override
	public List<Map<String, Object>> frontTypeList(Product product) {
	    List<Map<String, Object>> list = productRepository.frontTypeList(product.getPdcId());
	    return list.isEmpty() ? null : list;
	}

	// =============================================
	// frontRandTypeList
	// =============================================
	@Override
	public List<Map<String, Object>> frontRandTypeList(Product product) {
	    List<Map<String, Object>> list = productRepository.frontRandTypeList(product.getPdcId());
	    return list.isEmpty() ? null : list;
	}

}

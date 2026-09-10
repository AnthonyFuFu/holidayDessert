package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Product;
import com.holidaydessert.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductRepository productRepository;
	
	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Product product) {
		return productDao.list(product);
	}
	
	public int getCount(Product product) {
		return productDao.getCount(product);
	}
	
	public void add(Product product) {
		productDao.add(product);
	}
	
	public void update(Product product) {
		productDao.update(product);
	}
	
	public void delete(Product product) {
		productDao.delete(product);
	}
	
	public Product getData(Product product) {
		return productDao.getData(product);
	}
	
	public List<Map<String, Object>> getList() {
		return productDao.getList();
	}
	
	public List<Map<String, Object>> getPicList() {
		return productDao.getPicList();
	}
	
	public List<Map<String, Object>> issuePromotionList(Product product) {
		return productDao.issuePromotionList(product);
	}
	
	public List<Map<String, Object>> issueOneProductList() {
		return productDao.issueOneProductList();
	}
	
	public int getIssuePromotionCount(Product product) {
		return productDao.getIssuePromotionCount(product);
	}

	// =============================================
	// front
	// =============================================
	public ApiReturnObject getMainProductList() {
	    List<Map<String, Object>> list = productRepository.getMainProductList();
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無主要產品清單", null);
	    }
	    return ApiReturnObject.success("取得主要產品清單成功", list);
	}
	
	public ApiReturnObject getNewArrivalList() {
	    List<Map<String, Object>> list = productRepository.getNewArrivalList();
	    if (list.isEmpty()) {
	        return ApiReturnObject.success("查無新品上市清單", null);
	    }
	    return ApiReturnObject.success("取得新品上市清單成功", list);
	}
	
	public List<Map<String, Object>> frontTypeList(Product product) {
	    List<Map<String, Object>> list = productRepository.frontTypeList(product.getPdcId());
	    return list.isEmpty() ? null : list;
	}
	
	public List<Map<String, Object>> frontRandTypeList(Product product) {
	    List<Map<String, Object>> list = productRepository.frontRandTypeList(product.getPdcId());
	    return list.isEmpty() ? null : list;
	}
	
}

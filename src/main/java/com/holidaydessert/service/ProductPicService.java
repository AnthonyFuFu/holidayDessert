package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductPicDao;
import com.holidaydessert.model.ProductPic;
import com.holidaydessert.repository.ProductPicRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class ProductPicService {

	@Autowired
	private ProductPicDao productPicDao;
	
	@Autowired
	private ProductPicRepository productPicRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(ProductPic productPic) {
		return productPicDao.list(productPic);
	}
	
	public void add(ProductPic productPic) {
		productPicDao.add(productPic);
	}
	
	public void update(ProductPic productPic) {
		productPicDao.update(productPic);
	}
	
	public void delete(ProductPic productPic) {
		productPicDao.delete(productPic);
	}
	
	public ProductPic getData(ProductPic productPic) {
		return productPicDao.getData(productPic);
	}

	// =============================================
	// front
	// =============================================
	public Map<String, Object> frontRandList(Map<String, Object> params) {
		// 1. 取得基本參數
		String pdId = PageableUtil.getStringParam(params, "pdId", "").trim();
	    // 2. 建立只有分頁、沒有排序的 Pageable
	    Pageable pageable = PageableUtil.buildPageable(params);
		// 3. 查詢資料
		Page<ProductPic> productPicPage = productPicRepository.frontRandList(pdId, pageable);
		// 4. 取得查詢結果
		List<ProductPic> productPicList = productPicPage.getContent();
		// 5. 統一封裝回傳結果
		return PageableUtil.buildResult(productPicPage, productPicList);
	}
	
}

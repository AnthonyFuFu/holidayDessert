package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.ProductDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Product;
import com.holidaydessert.repository.ProductRepository;
import com.holidaydessert.utils.PageableUtil;

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
		// 查無資料時回傳空的 Product（Controller 會直接取用欄位）
		return productRepository.findDataById(product.getPdId()).orElseGet(Product::new);
	}
	
	public List<Map<String, Object>> getList() {
		return productDao.getList();
	}
	
	public List<Map<String, Object>> getPicList() {
		return productDao.getPicList();
	}
	
	public Map<String, Object> issuePromotionList(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.ISSUE_PROMOTION_TABLE,
				SortFieldsConstant.PRODUCT_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> productPage = productRepository.issuePromotionList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(productPage, PageableUtil.toMutableList(productPage), draw);
	}

	public List<Map<String, Object>> issueOneProductList() {
		return productDao.issueOneProductList();
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

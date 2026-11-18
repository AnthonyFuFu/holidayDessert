package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.PromotionDao;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.repository.PromotionRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class PromotionService {

	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired
	private PromotionRepository promotionRepository;

	// =============================================
	// back
	// =============================================
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.PROMOTION_TABLE,
				SortFieldsConstant.PROMOTION_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> promotionPage = promotionRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(promotionPage, PageableUtil.toMutableList(promotionPage), draw);
	}

	public void add(Promotion promotion) {
		promotionDao.add(promotion);
	}
	
	public void update(Promotion promotion) {
		promotionDao.update(promotion);
	}
	
	public void takeDown(Promotion promotion) {
		promotionDao.takeDown(promotion);
	}
	
	public List<Map<String, Object>> getList() {
		return promotionDao.getList();
	}

	// =============================================
	// front
	// =============================================
	public List<Map<String, Object>> nearestStartList(Promotion promotion) {
	    List<Map<String, Object>> list = promotionRepository.nearestStartList();
	    return list.isEmpty() ? null : list;
	}
	
	public Promotion getData(Promotion promotion) {
	    return promotionRepository.getData(promotion.getPmId()).orElse(null);
	}
	
}

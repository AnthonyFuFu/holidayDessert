package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.PromotionDetailDao;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.repository.PromotionDetailRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class PromotionDetailService {

	@Autowired
	private PromotionDetailDao promotionDetailDao;
	
	@Autowired
	private PromotionDetailRepository promotionDetailRepository;

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
				SortFieldsConstant.PROMOTION_DETAIL_TABLE,
				SortFieldsConstant.PROMOTION_DETAIL_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> promotionDetailPage = promotionDetailRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(promotionDetailPage, PageableUtil.toMutableList(promotionDetailPage), draw);
	}

	public void addOne(PromotionDetail promotionDetail) {
		promotionDetailDao.addOne(promotionDetail);
	}
	
	public void batchAddPromotion(Promotion promotion, String[] productId) {
		promotionDetailDao.batchAddPromotion(promotion, productId);
	}
	
	public void batchAddOneDayPromotion(Promotion promotion, String[] productId) {
		promotionDetailDao.batchAddOneDayPromotion(promotion, productId);
	}
	
	public void batchAddOneWeekPromotion(Promotion promotion, String[] productId) {
		promotionDetailDao.batchAddOneWeekPromotion(promotion, productId);
	}
	
	public void update(PromotionDetail promotionDetail) {
		promotionDetailDao.update(promotionDetail);
	}
	
	public PromotionDetail getData(PromotionDetail promotionDetail) {
		return promotionDetailDao.getData(promotionDetail);
	}

	// =============================================
	// front
	// =============================================
	public List<Map<String, Object>> frontList(PromotionDetail promotionDetail) {
	    List<Map<String, Object>> list = promotionDetailRepository.frontList(promotionDetail.getPmId());
	    return list.isEmpty() ? null : list;
	}
	
}

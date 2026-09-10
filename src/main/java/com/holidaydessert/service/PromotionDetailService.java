package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.PromotionDetailDao;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.repository.PromotionDetailRepository;

@Service
public class PromotionDetailService {

	@Autowired
	private PromotionDetailDao promotionDetailDao;
	
	@Autowired
	private PromotionDetailRepository promotionDetailRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(PromotionDetail promotionDetail) {
		return promotionDetailDao.list(promotionDetail);
	}
	
	public int getCount(PromotionDetail promotionDetail) {
		return promotionDetailDao.getCount(promotionDetail);
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

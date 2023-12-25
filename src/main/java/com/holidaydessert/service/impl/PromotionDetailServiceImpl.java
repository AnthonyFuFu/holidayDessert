package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.PromotionDetailDao;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.service.PromotionDetailService;

@Service
public class PromotionDetailServiceImpl implements PromotionDetailService {

	@Autowired
	private PromotionDetailDao promotionDetailDao;

	@Override
	public List<Map<String, Object>> list(PromotionDetail promotionDetail) {
		return promotionDetailDao.list(promotionDetail);
	}

	@Override
	public int getCount(PromotionDetail promotionDetail) {
		return promotionDetailDao.getCount(promotionDetail);
	}

	@Override
	public void addOne(PromotionDetail promotionDetail) {
		promotionDetailDao.addOne(promotionDetail);
	}

	@Override
	public void batchAddPromotion(Promotion promotion, String[] productId) {
		promotionDetailDao.batchAddPromotion(promotion, productId);
	}

	@Override
	public void batchAddOneDayPromotion(Promotion promotion, String[] productId) {
		promotionDetailDao.batchAddOneDayPromotion(promotion, productId);
	}

	@Override
	public void batchAddOneWeekPromotion(Promotion promotion, String[] productId) {
		promotionDetailDao.batchAddOneWeekPromotion(promotion, productId);
	}

	@Override
	public void update(PromotionDetail promotionDetail) {
		promotionDetailDao.update(promotionDetail);
	}

	@Override
	public PromotionDetail getData(PromotionDetail promotionDetail) {
		return promotionDetailDao.getData(promotionDetail);
	}
	
	@Override
	public List<Map<String, Object>> frontList(PromotionDetail promotionDetail) {
		return promotionDetailDao.frontList(promotionDetail);
	}

}

package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;

public interface PromotionDetailDao {

	// back
	public List<Map<String, Object>> list(PromotionDetail promotionDetail);
	public int getCount(PromotionDetail promotionDetail);
	public void addOne(PromotionDetail promotionDetail);
	public void batchAddPromotion(Promotion promotion, String[] productId);
	public void batchAddOneDayPromotion(Promotion promotion, String[] productId);
	public void batchAddOneWeekPromotion(Promotion promotion, String[] productId);
	public void update(PromotionDetail promotionDetail);
	public PromotionDetail getData(PromotionDetail promotionDetail);
	
	// front
	public List<Map<String, Object>> frontList(PromotionDetail promotionDetail);
	
}

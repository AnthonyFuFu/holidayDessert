package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.PromotionDetail;

public interface PromotionDetailDao {

	// back
	public List<Map<String, Object>> list(PromotionDetail promotionDetail);
	public int getCount(PromotionDetail promotionDetail);
	public void add(PromotionDetail promotionDetail);
	public void update(PromotionDetail promotionDetail);
	public void delete(PromotionDetail promotionDetail);
	
	// front
	public List<Map<String, Object>> frontNewList(PromotionDetail promotionDetail);
	public List<Map<String, Object>> frontTypeList(PromotionDetail promotionDetail);
	public List<Map<String, Object>> frontRandTypeList(PromotionDetail promotionDetail);
	
}

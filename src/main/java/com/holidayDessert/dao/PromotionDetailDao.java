package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.PromotionDetail;

public interface PromotionDetailDao {

	public List<Map<String, Object>> list(PromotionDetail promotionDetail);

}

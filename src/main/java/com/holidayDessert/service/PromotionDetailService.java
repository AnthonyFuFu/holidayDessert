package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.PromotionDetail;

public interface PromotionDetailService {

	public List<Map<String, Object>> list(PromotionDetail promotionDetail);

}

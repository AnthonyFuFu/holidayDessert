package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Promotion;

public interface PromotionService {

	public List<Map<String, Object>> list(Promotion promotion);

}

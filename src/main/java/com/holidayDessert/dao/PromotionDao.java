package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Promotion;

public interface PromotionDao {

	public List<Map<String, Object>> list(Promotion promotion);

}

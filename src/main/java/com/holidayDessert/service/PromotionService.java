package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Promotion;

public interface PromotionService {
	
	// back
	public List<Map<String, Object>> list(Promotion promotion);
	public int getCount(Promotion promotion);
	public void add(Promotion promotion);
	public void update(Promotion promotion);
	public void delete(Promotion promotion);
	
	// front
	public List<Map<String, Object>> frontNewList(Promotion promotion);
	public List<Map<String, Object>> frontTypeList(Promotion promotion);
	public List<Map<String, Object>> frontRandTypeList(Promotion promotion);
	
}

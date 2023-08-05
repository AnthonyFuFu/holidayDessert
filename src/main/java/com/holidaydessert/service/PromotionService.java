package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Promotion;

public interface PromotionService {
	
	// back
	public List<Map<String, Object>> list(Promotion promotion);
	public int getCount(Promotion promotion);
	public void add(Promotion promotion);
	public void update(Promotion promotion);
	public void takeDown(Promotion promotion);
	
	// front
	public List<Map<String, Object>> nearestStartList(Promotion promotion);
	
}

package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.PromotionDao;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.repository.PromotionRepository;

@Service
public class PromotionService {

	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired
	private PromotionRepository promotionRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Promotion promotion) {
		return promotionDao.list(promotion);
	}
	
	public int getCount(Promotion promotion) {
		return promotionDao.getCount(promotion);
	}
	
	public void add(Promotion promotion) {
		promotionDao.add(promotion);
	}
	
	public void update(Promotion promotion) {
		promotionDao.update(promotion);
	}
	
	public void takeDown(Promotion promotion) {
		promotionDao.takeDown(promotion);
	}
	
	public List<Map<String, Object>> getList() {
		return promotionDao.getList();
	}

	// =============================================
	// front
	// =============================================
	public List<Map<String, Object>> nearestStartList(Promotion promotion) {
	    List<Map<String, Object>> list = promotionRepository.nearestStartList();
	    return list.isEmpty() ? null : list;
	}
	
	public Promotion getData(Promotion promotion) {
	    return promotionRepository.getData(promotion.getPmId()).orElse(null);
	}
	
}

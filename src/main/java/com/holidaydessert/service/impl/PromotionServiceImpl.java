package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.PromotionDao;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionDao promotionDao;

	@Override
	public List<Map<String, Object>> list(Promotion promotion) {
		return promotionDao.list(promotion);
	}

	@Override
	public int getCount(Promotion promotion) {
		return promotionDao.getCount(promotion);
	}

	@Override
	public void add(Promotion promotion) {
		promotionDao.add(promotion);
	}

	@Override
	public void update(Promotion promotion) {
		promotionDao.update(promotion);
	}

	@Override
	public void takeDown(Promotion promotion) {
		promotionDao.takeDown(promotion);
	}

	@Override
	public List<Map<String, Object>> getList() {
		return promotionDao.getList();
	}

	@Override
	public List<Map<String, Object>> nearestStartList(Promotion promotion) {
		return promotionDao.nearestStartList(promotion);
	}

}

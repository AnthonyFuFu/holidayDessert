package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.PromotionDao;
import com.holidayDessert.model.Promotion;
import com.holidayDessert.service.PromotionService;

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
	public void delete(Promotion promotion) {
		promotionDao.delete(promotion);
	}

	@Override
	public List<Map<String, Object>> frontNewList(Promotion promotion) {
		return promotionDao.frontNewList(promotion);
	}

	@Override
	public List<Map<String, Object>> frontTypeList(Promotion promotion) {
		return promotionDao.frontTypeList(promotion);
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(Promotion promotion) {
		return promotionDao.frontRandTypeList(promotion);
	}

}

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

}

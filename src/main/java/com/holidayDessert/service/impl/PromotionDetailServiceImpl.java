package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.PromotionDetailDao;
import com.holidayDessert.model.PromotionDetail;
import com.holidayDessert.service.PromotionDetailService;

@Service
public class PromotionDetailServiceImpl implements PromotionDetailService {

	@Autowired
	private PromotionDetailDao promotionDetailDao;

	@Override
	public List<Map<String, Object>> list(PromotionDetail promotionDetail) {
		return promotionDetailDao.list(promotionDetail);
	}

}

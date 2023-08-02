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

	@Override
	public int getCount(PromotionDetail promotionDetail) {
		return promotionDetailDao.getCount(promotionDetail);
	}

	@Override
	public void add(PromotionDetail promotionDetail) {
		promotionDetailDao.add(promotionDetail);
	}

	@Override
	public void update(PromotionDetail promotionDetail) {
		promotionDetailDao.update(promotionDetail);
	}

	@Override
	public void delete(PromotionDetail promotionDetail) {
		promotionDetailDao.delete(promotionDetail);
	}

	@Override
	public List<Map<String, Object>> frontNewList(PromotionDetail promotionDetail) {
		return promotionDetailDao.frontNewList(promotionDetail);
	}

	@Override
	public List<Map<String, Object>> frontTypeList(PromotionDetail promotionDetail) {
		return promotionDetailDao.frontTypeList(promotionDetail);
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(PromotionDetail promotionDetail) {
		return promotionDetailDao.frontRandTypeList(promotionDetail);
	}

}

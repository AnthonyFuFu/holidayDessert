package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.CouponDao;
import com.holidayDessert.model.Coupon;
import com.holidayDessert.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService{
	
	@Autowired
	private CouponDao couponDao;

	@Override
	public List<Map<String, Object>> list(Coupon coupon) {
		return couponDao.list(coupon);
	}
	
}

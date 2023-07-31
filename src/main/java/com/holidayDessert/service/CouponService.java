package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Coupon;

public interface CouponService {
	
	public List<Map<String, Object>> list(Coupon coupon);
	
}

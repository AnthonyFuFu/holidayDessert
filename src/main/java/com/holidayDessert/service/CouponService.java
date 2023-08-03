package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Coupon;

public interface CouponService {
	
	// back
	public List<Map<String, Object>> list(Coupon coupon);
	public int getCount(Coupon coupon);
	public void add(Coupon coupon);
	public void update(Coupon coupon);
	public void takeDown(Coupon coupon);
	
}

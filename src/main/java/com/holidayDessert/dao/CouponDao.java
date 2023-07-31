package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Coupon;

public interface CouponDao {
	
	public List<Map<String, Object>> list(Coupon coupon);
	
}

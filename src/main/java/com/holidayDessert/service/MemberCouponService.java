package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.MemberCoupon;

public interface MemberCouponService {

	public List<Map<String, Object>> list(MemberCoupon memberCoupon);

}

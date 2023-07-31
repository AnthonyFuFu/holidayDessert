package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.MemberCoupon;

public interface MemberCouponDao {

	public List<Map<String, Object>> list(MemberCoupon memberCoupon);

}

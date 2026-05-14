package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.MemberCoupon;

public interface MemberCouponDao {

	// back
	public List<Map<String, Object>> list(MemberCoupon memberCoupon);
	public int getCount(MemberCoupon memberCoupon);
	public void batchAddOneDayCoupon(Coupon coupon, String[] member);
	public void batchAddOneWeekCoupon(Coupon coupon, String[] member);
	
}

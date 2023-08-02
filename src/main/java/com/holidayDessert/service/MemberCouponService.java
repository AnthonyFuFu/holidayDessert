package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Coupon;
import com.holidayDessert.model.Member;
import com.holidayDessert.model.MemberCoupon;

public interface MemberCouponService {
	
	// back
	public List<Map<String, Object>> list(MemberCoupon memberCoupon);
	public int getCount(MemberCoupon memberCoupon);
	public void update(MemberCoupon memberCoupon);
	public void delete(MemberCoupon memberCoupon);
	public void batchAdd(Coupon coupon, String[] member);
	
	// front
	public void receiveCoupon(Coupon coupon, Member member);
	public List<Map<String, Object>> listMemberCoupon(MemberCoupon memberCoupon);
	
}

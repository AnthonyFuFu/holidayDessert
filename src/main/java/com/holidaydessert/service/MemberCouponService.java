package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.MemberCoupon;

public interface MemberCouponService {
	
	// back
	public List<Map<String, Object>> list(MemberCoupon memberCoupon);
	public int getCount(MemberCoupon memberCoupon);
	public void batchAddOneDayCoupon(Coupon coupon, String[] member);
	public void batchAddOneWeekCoupon(Coupon coupon, String[] member);
	
	// front
	public void useCoupon(MemberCoupon memberCoupon);
	public void receiveOneDayCoupon(Coupon coupon, Member member);
	public void receiveOneWeekCoupon(Coupon coupon, Member member);
	public void receiveOneMonthCoupon(Coupon coupon, Member member);
	public List<Map<String, Object>> listMemberCoupon(MemberCoupon memberCoupon);
	
}

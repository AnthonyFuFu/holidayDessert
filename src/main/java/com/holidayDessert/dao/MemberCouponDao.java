package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Coupon;
import com.holidayDessert.model.Member;
import com.holidayDessert.model.MemberCoupon;

public interface MemberCouponDao {

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

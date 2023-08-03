package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.MemberCouponDao;
import com.holidayDessert.model.Coupon;
import com.holidayDessert.model.Member;
import com.holidayDessert.model.MemberCoupon;
import com.holidayDessert.service.MemberCouponService;

@Service
public class MemberCouponServiceImpl implements MemberCouponService {

	@Autowired
	private MemberCouponDao memberCouponDao;
	
	@Override
	public List<Map<String, Object>> list(MemberCoupon memberCoupon) {
		return memberCouponDao.list(memberCoupon);
	}

	@Override
	public int getCount(MemberCoupon memberCoupon) {
		return memberCouponDao.getCount(memberCoupon);
	}

	@Override
	public void batchAddOneDayCoupon(Coupon coupon, String[] member) {
		memberCouponDao.batchAddOneDayCoupon(coupon, member);
	}

	@Override
	public void batchAddOneWeekCoupon(Coupon coupon, String[] member) {
		memberCouponDao.batchAddOneWeekCoupon(coupon, member);
	}
	
	@Override
	public void useCoupon(MemberCoupon memberCoupon) {
		memberCouponDao.useCoupon(memberCoupon);
	}

	@Override
	public void receiveOneDayCoupon(Coupon coupon, Member member) {
		memberCouponDao.receiveOneDayCoupon(coupon, member);
	}

	@Override
	public void receiveOneWeekCoupon(Coupon coupon, Member member) {
		memberCouponDao.receiveOneWeekCoupon(coupon, member);
	}
	
	@Override
	public void receiveOneMonthCoupon(Coupon coupon, Member member) {
		memberCouponDao.receiveOneMonthCoupon(coupon, member);
	}
	
	@Override
	public List<Map<String, Object>> listMemberCoupon(MemberCoupon memberCoupon) {
		return memberCouponDao.listMemberCoupon(memberCoupon);
	}

}

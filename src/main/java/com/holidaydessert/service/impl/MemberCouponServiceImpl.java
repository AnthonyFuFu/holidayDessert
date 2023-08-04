package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.MemberCouponDao;
import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.MemberCoupon;
import com.holidaydessert.service.MemberCouponService;

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

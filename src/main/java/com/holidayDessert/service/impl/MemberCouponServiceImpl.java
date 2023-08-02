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
	public void update(MemberCoupon memberCoupon) {
		memberCouponDao.update(memberCoupon);
	}

	@Override
	public void delete(MemberCoupon memberCoupon) {
		memberCouponDao.delete(memberCoupon);
	}

	@Override
	public void batchAdd(Coupon coupon, String[] member) {
		memberCouponDao.batchAdd(coupon, member);
	}

	@Override
	public void receiveCoupon(Coupon coupon, Member member) {
		memberCouponDao.receiveCoupon(coupon, member);
	}

	@Override
	public List<Map<String, Object>> listMemberCoupon(MemberCoupon memberCoupon) {
		return memberCouponDao.listMemberCoupon(memberCoupon);
	}

}

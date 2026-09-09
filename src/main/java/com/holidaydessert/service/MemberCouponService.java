package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.MemberCouponDao;
import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.MemberCoupon;
import com.holidaydessert.repository.MemberCouponRepository;

@Service
public class MemberCouponService {

	@Autowired
	private MemberCouponDao memberCouponDao;

	@Autowired
	private MemberCouponRepository memberCouponRepository;

	// back
	public List<Map<String, Object>> list(MemberCoupon memberCoupon) {
		return memberCouponDao.list(memberCoupon);
	}

	public int getCount(MemberCoupon memberCoupon) {
		return memberCouponDao.getCount(memberCoupon);
	}

	public void batchAddOneDayCoupon(Coupon coupon, String[] member) {
		memberCouponDao.batchAddOneDayCoupon(coupon, member);
	}

	public void batchAddOneWeekCoupon(Coupon coupon, String[] member) {
		memberCouponDao.batchAddOneWeekCoupon(coupon, member);
	}

	// front
	@Transactional
	public void useCoupon(MemberCoupon memberCoupon) {
		memberCouponRepository.useCoupon(memberCoupon.getMemCpId());
	}

	@Transactional
	public void receiveOneDayCoupon(Coupon coupon, Member member) {
		memberCouponRepository.receiveOneDayCoupon(member.getMemId(), coupon.getCpId());
	}

	@Transactional
	public void receiveOneWeekCoupon(Coupon coupon, Member member) {
		memberCouponRepository.receiveOneWeekCoupon(member.getMemId(), coupon.getCpId());
	}

	@Transactional
	public void receiveOneMonthCoupon(Coupon coupon, Member member) {
		memberCouponRepository.receiveOneMonthCoupon(member.getMemId(), coupon.getCpId());
	}

	public List<Map<String, Object>> listMemberCoupon(String memId) {
		List<Map<String, Object>> list = memberCouponRepository.listMemberCoupon(memId);
		return list.isEmpty() ? null : list;
	}
	
}

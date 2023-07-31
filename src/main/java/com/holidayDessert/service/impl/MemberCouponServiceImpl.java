package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.MemberCouponDao;
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

}

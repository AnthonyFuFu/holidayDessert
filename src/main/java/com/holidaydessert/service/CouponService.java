package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.CouponDao;
import com.holidaydessert.model.Coupon;

@Service
public class CouponService {

	@Autowired
	private CouponDao couponDao;

	// back
	public List<Map<String, Object>> list(Coupon coupon) {
		return couponDao.list(coupon);
	}

	public int getCount(Coupon coupon) {
		return couponDao.getCount(coupon);
	}

	public void add(Coupon coupon) {
		couponDao.add(coupon);
	}

	public void update(Coupon coupon) {
		couponDao.update(coupon);
	}

	public void takeDown(Coupon coupon) {
		couponDao.takeDown(coupon);
	}

	public Coupon getData(Coupon coupon) {
		return couponDao.getData(coupon);
	}

	public List<Map<String, Object>> getList() {
		return couponDao.getList();
	}
	
}

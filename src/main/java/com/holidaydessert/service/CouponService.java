package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.CouponDao;
import com.holidaydessert.model.Coupon;
import com.holidaydessert.repository.CouponRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponDao couponDao;

	// =============================================
	// back
	// =============================================
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.COUPON_TABLE,
				SortFieldsConstant.COUPON_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> couponPage = couponRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(couponPage, PageableUtil.toMutableList(couponPage), draw);
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

package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.MemberCouponDao;
import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.MemberCoupon;
import com.holidaydessert.repository.MemberCouponRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class MemberCouponService {

	@Autowired
	private MemberCouponDao memberCouponDao;

	@Autowired
	private MemberCouponRepository memberCouponRepository;

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
				SortFieldsConstant.MEMBER_COUPON_TABLE,
				SortFieldsConstant.MEMBER_COUPON_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> memberCouponPage = memberCouponRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(memberCouponPage, PageableUtil.toMutableList(memberCouponPage), draw);
	}

	public void batchAddOneDayCoupon(Coupon coupon, String[] member) {
		memberCouponDao.batchAddOneDayCoupon(coupon, member);
	}

	public void batchAddOneWeekCoupon(Coupon coupon, String[] member) {
		memberCouponDao.batchAddOneWeekCoupon(coupon, member);
	}

	// =============================================
	// front
	// =============================================
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

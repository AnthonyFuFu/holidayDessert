package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.MemberCouponDao;
import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.MemberCoupon;

@Repository
public class MemberCouponDaoImpl implements MemberCouponDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(MemberCoupon memberCoupon) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT MEM_NAME, DATE_FORMAT(MEM_CP_START, '%Y-%m-%d %H:%i:%s') MEM_CP_START, DATE_FORMAT(MEM_CP_END, '%Y-%m-%d %H:%i:%s') MEM_CP_END, "
				   + " MEM_CP_STATUS, MEM_CP_RECORD, CP_NAME,CP_DISCOUNT "
				   + " FROM holiday_dessert.member_coupon mc "
				   + " LEFT JOIN member m ON m.MEM_ID = mc.MEM_ID "
				   + " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID ";
		
		if (memberCoupon.getSearchText() != null && memberCoupon.getSearchText().length() > 0) {
			String[] searchText = memberCoupon.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(MEM_CP_STATUS, ?) > 0 "
					+  " OR INSTR(CP_NAME, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(MemberCoupon memberCoupon) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.member_coupon mc "
				   + " LEFT JOIN member m ON m.MEM_ID = mc.MEM_ID "
				   + " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID ";
		
		if (memberCoupon.getSearchText() != null && memberCoupon.getSearchText().length() > 0) {
			String[] searchText = memberCoupon.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(MEM_CP_STATUS, ?) > 0 "
					+  " OR INSTR(CP_NAME, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void batchAddOneDayCoupon(Coupon coupon, String[] member) {
		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.member_coupon "
				   + " (MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) ";
		
		if(member.length > 0) {
			for(int i=0; i<member.length; i++) {
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), 1) ";
				} else {
					sql += " ,(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), 1) ";
				}
				args.add(member[i]);
				args.add(coupon.getCpId());
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void batchAddOneWeekCoupon(Coupon coupon, String[] member) {
		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.member_coupon "
				   + " (MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) ";
		
		if(member.length > 0) {
			for(int i=0; i<member.length; i++) {
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), 1) ";
				} else {
					sql += " ,(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), 1) ";
				}
				args.add(member[i]);
				args.add(coupon.getCpId());
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}
	
	@Override
	public void useCoupon(MemberCoupon memberCoupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.member_coupon "
				   + " SET MEM_CP_STATUS = 0, MEM_CP_RECORD = NOW() "
				   + " WHERE MEM_CP_ID = ? ";
		
		args.add(memberCoupon.getMemCpId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void receiveOneDayCoupon(Coupon coupon, Member member) {

		String sql = " INSERT INTO holiday_dessert.member_coupon "
				   + " (MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) "
				   + " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), 1) ";
		
		jdbcTemplate.update(sql, new Object[] { member.getMemId(), coupon.getCpId() });
		
	}

	@Override
	public void receiveOneWeekCoupon(Coupon coupon, Member member) {

		String sql = " INSERT INTO holiday_dessert.member_coupon "
				   + " (MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) "
				   + " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), 1) ";
		
		jdbcTemplate.update(sql, new Object[] { member.getMemId(), coupon.getCpId() });
		
	}

	@Override
	public void receiveOneMonthCoupon(Coupon coupon, Member member) {

		String sql = " INSERT INTO holiday_dessert.member_coupon "
				   + " (MEM_ID, CP_ID, MEM_CP_START, MEM_CP_END, MEM_CP_STATUS) "
				   + " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 MONTH), ' 23:59:59'), 1) ";
		
		jdbcTemplate.update(sql, new Object[] { member.getMemId(), coupon.getCpId() });
		
	}
	
	@Override
	public List<Map<String, Object>> listMemberCoupon(MemberCoupon memberCoupon) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT MEM_NAME, DATE_FORMAT(MEM_CP_START, '%Y-%m-%d %H:%i:%s') MEM_CP_START, DATE_FORMAT(MEM_CP_END, '%Y-%m-%d %H:%i:%s') MEM_CP_END, "
				   + " MEM_CP_ID, MEM_CP_STATUS, MEM_CP_RECORD, CP_NAME,CP_DISCOUNT "
				   + " FROM holiday_dessert.member_coupon mc "
				   + " LEFT JOIN member m ON m.MEM_ID = mc.MEM_ID "
				   + " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID "
				   + " WHERE mc.MEM_ID = ? "
				   + " AND CURDATE() BETWEEN MEM_CP_START AND MEM_CP_END ";
		
		args.add(memberCoupon.getMemId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}
	
}

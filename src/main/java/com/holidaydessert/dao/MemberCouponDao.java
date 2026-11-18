package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Coupon;

@Repository
public class MemberCouponDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// =============================================
	// back
	// =============================================
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
	
}

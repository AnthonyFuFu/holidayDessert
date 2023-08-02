package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.MemberCouponDao;
import com.holidayDessert.model.Coupon;
import com.holidayDessert.model.Member;
import com.holidayDessert.model.MemberCoupon;

@Repository
public class MemberCouponDaoImpl implements MemberCouponDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(MemberCoupon memberCoupon) {

		String sql = " SELECT * FROM holiday_dessert.member_coupon ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(MemberCoupon memberCoupon) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(MemberCoupon memberCoupon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(MemberCoupon memberCoupon) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void batchAdd(Coupon coupon, String[] member) {
		List<Object> args = new ArrayList<Object>();
		
		String sql = " INSERT INTO holiday_dessert.member_coupon "
				   + " (FORECAST_ID, TYPE, DATA, CREATE_BY, CREATE_TIME) ";
		
		if(member.length > 0) {
			for(int i=0; i<member.length; i++) {
				if(i == 0) {
					sql += " VALUES(?, ?, ?, ?, NOW()) ";
				} else {
					sql += " ,(?, ?, ?, ?, NOW()) ";
				}
				args.add(coupon.getCpId());
//				args.add(forecastDetail.getType());
				args.add(member[i]);
//				args.add(forecastDetail.getCreate_by());
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void receiveCoupon(Coupon coupon, Member member) {
		
	}

	@Override
	public List<Map<String, Object>> listMemberCoupon(MemberCoupon memberCoupon) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

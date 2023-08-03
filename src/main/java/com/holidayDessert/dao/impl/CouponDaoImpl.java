package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.CouponDao;
import com.holidayDessert.model.Coupon;

@Repository
public class CouponDaoImpl implements CouponDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(Coupon coupon) {

		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM holiday_dessert.coupon ";
		
		if (coupon.getSearchText() != null && coupon.getSearchText().length() > 0) {
			String[] searchText = coupon.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(CP_NAME, ?) > 0"
					+  " OR INSTR(CP_DISCOUNT, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if (coupon.getStart() != null && !"".equals(coupon.getStart())) {
			sql += " LIMIT " + coupon.getStart() + "," + coupon.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public int getCount(Coupon coupon) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.coupon ";
		
		if (coupon.getSearchText() != null && coupon.getSearchText().length() > 0) {
			String[] searchText = coupon.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(CP_NAME, ?) > 0"
					+  " OR INSTR(CP_DISCOUNT, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(Coupon coupon) {

		String sql = " INSERT INTO holiday_dessert.coupon "
				   + " (CP_NAME, CP_DISCOUNT, CP_STATUS, CP_PIC) "
				   + " VALUES(?, ?, ?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] { coupon.getCpName(), coupon.getCpDiscount(), coupon.getCpStatus(), coupon.getCpPic() });
		
	}

	@Override
	public void update(Coupon coupon) {

		List<Object> args = new ArrayList<Object>();
		
		String sql = " UPDATE holiday_dessert.coupon "
				   + " SET CP_NAME = ? , CP_DISCOUNT = ?, CP_STATUS = ?, CP_PIC = ? "
				   + " WHERE CP_ID = ? ";
		
		args.add(coupon.getCpName());
		args.add(coupon.getCpDiscount());
		args.add(coupon.getCpStatus());
		args.add(coupon.getCpPic());
		args.add(coupon.getCpId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void takeDown(Coupon coupon) {
		
		String sql = " UPDATE holiday_dessert.coupon "
				   + " SET CP_STATUS = ? "
				   + " WHERE CP_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { coupon.getCpStatus(), coupon.getCpId() });
		
	}

}

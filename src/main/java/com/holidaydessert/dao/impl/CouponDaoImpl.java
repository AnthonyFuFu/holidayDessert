package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.CouponDao;
import com.holidaydessert.model.Coupon;

@Repository
public class CouponDaoImpl implements CouponDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT *, "
				   + " CASE CP_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.coupon "
				   + " ) AS subquery ";
		
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
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
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
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( SELECT *,"
				   + " CASE CP_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.coupon "
				   + " ) AS subquery ";
		
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
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.coupon "
				   + " (CP_NAME, CP_DISCOUNT, CP_STATUS, CP_PICTURE, CP_IMAGE) "
				   + " VALUES(?, ?, ?, ?, ?) ";

		args.add(coupon.getCpName());
		args.add(coupon.getCpDiscount());
		args.add(coupon.getCpStatus());
		args.add(coupon.getCpPicture());
		args.add(coupon.getCpImage());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.coupon "
				   + " SET CP_NAME = ?, CP_DISCOUNT = ?, CP_STATUS = ?, CP_PICTURE = ?, CP_IMAGE = ? "
				   + " WHERE CP_ID = ? ";
		
		args.add(coupon.getCpName());
		args.add(coupon.getCpDiscount());
		args.add(coupon.getCpStatus());
		args.add(coupon.getCpPicture());
		args.add(coupon.getCpImage());
		args.add(coupon.getCpId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void takeDown(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.coupon "
				   + " SET CP_STATUS = ? "
				   + " WHERE CP_ID = ? ";

		args.add(coupon.getCpStatus());
		args.add(coupon.getCpId());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public Coupon getData(Coupon coupon) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.coupon "
				   + " WHERE CP_ID = ? ";
		
		args.add(coupon.getCpId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		Coupon item = new Coupon();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        String cpId = String.valueOf(resultMap.get("CP_ID"));
	        String cpName = String.valueOf(resultMap.get("CP_NAME"));
	        String cpDiscount = String.valueOf(resultMap.get("CP_DISCOUNT"));
	        String cpStatus = String.valueOf(resultMap.get("CP_STATUS"));
	        String cpPicture = String.valueOf(resultMap.get("CP_PICTURE"));
	        String cpImage = String.valueOf(resultMap.get("CP_IMAGE"));
	        
	        item.setCpId(cpId);
	        item.setCpName(cpName);
	        item.setCpDiscount(cpDiscount);
	        item.setCpStatus(cpStatus);
	        item.setCpPicture(cpPicture);
	        item.setCpImage(cpImage);
	        
	    }
		return item == null ? null : item;
	}

	@Override
	public List<Map<String, Object>> getList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.coupon "
				   + " WHERE CP_STATUS = '1' ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

}

package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.OrderDetailDao;
import com.holidaydessert.model.OrderDetail;

@Repository
public class OrderDetailDaoImpl implements OrderDetailDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(OrderDetail orderDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT ORD_SUBTOTAL, ORD_TOTAL, ORD_STATUS, DATE_FORMAT(ORD_CREATE, '%Y-%m-%d') ORD_CREATE, ORD_RECIPIENT, ORD_RECIPIENT_PHONE, "
				   + " ORD_PAYMENT, ORD_DELIVERY, ORD_ADDRESS, ORD_NOTE, ORD_DELIVERY_FEE, od.*, PD_NAME "
				   + " FROM holiday_dessert.order_detail od "
				   + " LEFT JOIN main_order mo ON od.ORD_ID = mo.ORD_ID "
				   + " LEFT JOIN product p ON p.PD_ID = od.PD_ID ";

		if (orderDetail.getSearchText() != null && orderDetail.getSearchText().length() > 0) {
			String[] searchText = orderDetail.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(ORD_STATUS, ?) > 0"
					+  " OR INSTR(ORD_RECIPIENT, ?) > 0 "
					+  " OR INSTR(ORD_RECIPIENT_PHONE, ?) > 0 "
					+  " OR INSTR(ORD_PAYMENT, ?) > 0 "
					+  " OR INSTR(ORD_DELIVERY, ?) > 0 "
					+  " OR INSTR(ORD_NOTE, ?) > 0 "
					+  " OR INSTR(ORD_DELIVERY_FEE, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if (orderDetail.getStart() != null && !"".equals(orderDetail.getStart())) {
			sql += " LIMIT " + orderDetail.getStart() + "," + orderDetail.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(OrderDetail orderDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.order_detail od "
				   + " LEFT JOIN main_order mo ON od.ORD_ID = mo.ORD_ID "
				   + " LEFT JOIN product p ON p.PD_ID = od.PD_ID ";

		if (orderDetail.getSearchText() != null && orderDetail.getSearchText().length() > 0) {
			String[] searchText = orderDetail.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(ORD_STATUS, ?) > 0"
					+  " OR INSTR(ORD_RECIPIENT, ?) > 0 "
					+  " OR INSTR(ORD_RECIPIENT_PHONE, ?) > 0 "
					+  " OR INSTR(ORD_PAYMENT, ?) > 0 "
					+  " OR INSTR(ORD_DELIVERY, ?) > 0 "
					+  " OR INSTR(ORD_NOTE, ?) > 0 "
					+  " OR INSTR(ORD_DELIVERY_FEE, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public List<Map<String, Object>> frontOrderDetails(OrderDetail orderDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT od.*, PD_NAME "
				   + " FROM holiday_dessert.order_detail od "
				   + " LEFT JOIN main_order mo ON od.ORD_ID = mo.ORD_ID "
				   + " LEFT JOIN product p ON p.PD_ID = od.PD_ID "
				   + " WHERE od.ORD_ID = ? ";
		
		args.add(orderDetail.getOrdId());

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public void add(OrderDetail orderDetail) {

		String sql = " INSERT INTO holiday_dessert.order_detail "
				   + " (ORD_ID, PD_ID, ORDD_NUMBER, ORDD_PRICE, ORDD_DISCOUNT_PRICE) "
				   + " VALUES(?, ?, ?, ?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] {orderDetail.getOrdId(), orderDetail.getPdId(), orderDetail.getOrddNumber(), orderDetail.getOrddPrice(), orderDetail.getOrddDiscountPrice() });
		
	}

}

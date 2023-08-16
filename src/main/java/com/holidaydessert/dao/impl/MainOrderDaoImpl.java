package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.MainOrderDao;
import com.holidaydessert.model.MainOrder;

@Repository
public class MainOrderDaoImpl implements MainOrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(MainOrder mainOrder) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT MEM_ACCOUNT, "
				   + " CASE MEM_GENDER "
				   + " WHEN 'm' THEN '男' "
				   + " WHEN 'f' THEN '女' "
				   + " END AS MEM_GENDER, "
				   + " MEM_EMAIL, ORD_ID, mo.MEM_ID, MEM_CP_ID, ORD_SUBTOTAL, ORD_TOTAL, ORD_STATUS, "
				   + " DATE_FORMAT(ORD_CREATE, '%Y-%m-%d') ORD_CREATE, ORD_RECIPIENT, ORD_RECIPIENT_PHONE, "
				   + " ORD_PAYMENT, ORD_DELIVERY, ORD_ADDRESS, ORD_NOTE, ORD_DELIVERY_FEE, "
				   + " CASE WHEN mo.MEM_CP_ID IS NULL THEN '未使用' "
				   + " ELSE (SELECT CP_NAME "
				   + " FROM member_coupon mc "
				   + " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID "
				   + " WHERE mc.MEM_CP_ID = mo.MEM_CP_ID "
				   + " LIMIT 1) "
				   + " END AS COUPON_USED "
				   + " FROM holiday_dessert.main_order mo"
				   + " LEFT JOIN member m ON m.MEM_ID = mo.MEM_ID"
				   + ") AS subquery ";
		
		if (mainOrder.getSearchText() != null && mainOrder.getSearchText().length() > 0) {
			String[] searchText = mainOrder.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_ACCOUNT, ?) > 0"
					+  " OR INSTR(MEM_GENDER, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(ORD_RECIPIENT, ?) > 0 "
					+  " OR INSTR(ORD_RECIPIENT_PHONE, ?) > 0 "
					+  " OR INSTR(ORD_ADDRESS, ?) > 0 "
					+  " OR INSTR(ORD_NOTE, ?) > 0 "
					+  " OR INSTR(COUPON_USED, ?) > 0 "
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
		
		if (mainOrder.getStart() != null && !"".equals(mainOrder.getStart())) {
			sql += " LIMIT " + mainOrder.getStart() + "," + mainOrder.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(MainOrder mainOrder) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( "
				   + " SELECT * FROM ( SELECT MEM_ACCOUNT, "
				   + " CASE MEM_GENDER "
				   + " WHEN 'm' THEN '男' "
				   + " WHEN 'f' THEN '女' "
				   + " END AS MEM_GENDER, "
				   + " MEM_EMAIL, mo.*, "
				   + " CASE WHEN mo.MEM_CP_ID IS NULL THEN '未使用' "
				   + " ELSE (SELECT CP_NAME "
				   + " FROM member_coupon mc "
				   + " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID "
				   + " WHERE mc.MEM_CP_ID = mo.MEM_CP_ID "
				   + " LIMIT 1) "
				   + " END AS COUPON_USED "
				   + " FROM holiday_dessert.main_order mo"
				   + " LEFT JOIN member m ON m.MEM_ID = mo.MEM_ID"
				   + ") AS subquery ";
		
		if (mainOrder.getSearchText() != null && mainOrder.getSearchText().length() > 0) {
			String[] searchText = mainOrder.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_ACCOUNT, ?) > 0"
					+  " OR INSTR(MEM_GENDER, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(ORD_RECIPIENT, ?) > 0 "
					+  " OR INSTR(ORD_RECIPIENT_PHONE, ?) > 0 "
					+  " OR INSTR(ORD_ADDRESS, ?) > 0 "
					+  " OR INSTR(ORD_NOTE, ?) > 0 "
					+  " OR INSTR(COUPON_USED, ?) > 0 "
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
		sql += ") count ";
		
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void update(MainOrder mainOrder) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.main_order "
				   + " SET MEM_ID = ?, MEM_CP_ID = ?, ORD_SUBTOTAL = ?, ORD_TOTAL = ?, ORD_STATUS = ?, "
				   + " ORD_RECIPIENT = ?, ORD_RECIPIENT_PHONE = ?, ORD_PAYMENT = ?, "
				   + " ORD_DELIVERY = ?, ORD_ADDRESS = ?, ORD_NOTE = ?, ORD_DELIVERY_FEE = ? "
				   + " WHERE ORD_ID = ? ";
		
		args.add(mainOrder.getMemId());
		args.add(mainOrder.getMemCpId());
		args.add(mainOrder.getOrdSubtotal());
		args.add(mainOrder.getOrdTotal());
		args.add(mainOrder.getOrdStatus());
		args.add(mainOrder.getOrdRecipient());
		args.add(mainOrder.getOrdRecipientPhone());
		args.add(mainOrder.getOrdPayment());
		args.add(mainOrder.getOrdDelivery());
		args.add(mainOrder.getOrdAddress());
		args.add(mainOrder.getOrdNote());
		args.add(mainOrder.getOrdDeliveryFee());
		args.add(mainOrder.getOrdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void add(MainOrder mainOrder) {

		String sql = " INSERT INTO holiday_dessert.main_order "
				   + " (MEM_ID, MEM_CP_ID, ORD_SUBTOTAL, ORD_TOTAL, ORD_STATUS, ORD_CREATE, ORD_RECIPIENT, "
				   + " ORD_RECIPIENT_PHONE, ORD_PAYMENT, ORD_DELIVERY, ORD_ADDRESS, ORD_NOTE, ORD_DELIVERY_FEE) "
				   + " VALUES(?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sql, new Object[] { mainOrder.getMemId(), mainOrder.getMemCpId(),
				mainOrder.getOrdSubtotal(),mainOrder.getOrdTotal(), mainOrder.getOrdStatus(),
				mainOrder.getOrdRecipient(), mainOrder.getOrdRecipientPhone(),
				mainOrder.getOrdPayment(), mainOrder.getOrdDelivery(), mainOrder.getOrdAddress(),
				mainOrder.getOrdNote(), mainOrder.getOrdDeliveryFee() });
		
	}

	@Override
	public List<Map<String, Object>> getMemOrderList(MainOrder mainOrder) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT MEM_ACCOUNT, "
				   + " CASE MEM_GENDER "
				   + " WHEN 'm' THEN '男' "
				   + " WHEN 'f' THEN '女' "
				   + " END AS MEM_GENDER, "
				   + " MEM_EMAIL, mo.*, "
				   + " CASE WHEN mo.MEM_CP_ID IS NULL THEN '未使用' "
				   + " ELSE (SELECT CP_NAME "
				   + " FROM member_coupon mc "
				   + " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID "
				   + " WHERE mc.MEM_CP_ID = mo.MEM_CP_ID "
				   + " LIMIT 1) "
				   + " END AS COUPON_USED "
				   + " FROM holiday_dessert.main_order mo"
				   + " LEFT JOIN member m ON m.MEM_ID = mo.MEM_ID "
				   + " WHERE MEM_ID = ? ";
		
		args.add(mainOrder.getMemId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

}

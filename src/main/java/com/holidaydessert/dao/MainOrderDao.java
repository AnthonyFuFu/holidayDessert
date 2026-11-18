package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.MainOrder;

@Repository
public class MainOrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
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
	
}

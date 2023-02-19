package com.holiday_dessert.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MainOrder {

	private String ord_id;
	private String mem_id;
	private String mem_cp_id;
	private String ord_subtotal;
	private String ord_total;
	private String ord_status;
	private String ord_create;
	private String ord_recipient;
	private String ord_recipient_phone;
	private String ord_payment;
	private String ord_address;
	private String ord_note;
	private String ord_delivery_fee;
}

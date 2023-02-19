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
public class OrderDetail {
	
	private String ord_id;
	private String pd_id;
	private String ordd_number;
	private String ordd_price;
	private String ordd_discount_price;
	
}
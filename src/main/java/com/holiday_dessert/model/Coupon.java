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
public class Coupon {

	private String cp_id;
	private String cp_name;
	private String cp_discount;
	private String cp_status;
	private String cp_pic;

}

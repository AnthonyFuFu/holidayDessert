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
public class Product {

	private String pd_id;
	private String pdc_id;
	private String pd_name;
	private String pd_price;
	private String pd_description;
	private String pd_status;
	
}
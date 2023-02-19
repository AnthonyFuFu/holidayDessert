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
public class ReceiptInformation {
	
	private String rcp_id;
	private String mem_id;
	private String rcp_name;
	private String rcp_cvs;
	private String rcp_address;
	private String rcp_phone;
	
}
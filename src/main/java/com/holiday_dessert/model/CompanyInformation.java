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
public class CompanyInformation {

	private String com_id;
	private String com_name;
	private String com_address;
	private String com_phone;
	private String com_memo;
	
}
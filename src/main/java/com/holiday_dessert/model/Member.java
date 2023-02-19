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
public class Member {
	
	private String mem_id;
	private String mem_name;
	private String mem_account;
	private String mem_password;
	private String mem_gender;
	private String mem_phone;
	private String mem_email;
	private String mem_address;
	private String mem_birthday;
	private String mem_status;
	
}
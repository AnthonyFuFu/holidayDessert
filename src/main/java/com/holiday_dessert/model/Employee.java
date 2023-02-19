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
public class Employee {
	
	private String emp_id;
	private String emp_name;
	private String emp_phone;
	private String emp_picture;
	private String emp_account;
	private String emp_password;
	private String emp_level;
	private String emp_status;
	private String emp_hiredate;
	
}
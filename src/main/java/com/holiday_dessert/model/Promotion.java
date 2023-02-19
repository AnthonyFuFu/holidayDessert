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
public class Promotion {

	private String pm_id;
	private String pm_name;
	private String pm_description;
	private String pm_discount;
	private String pm_status;
}
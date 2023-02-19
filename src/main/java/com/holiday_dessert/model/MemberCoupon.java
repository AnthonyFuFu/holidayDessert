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
public class MemberCoupon {

	private String mem_cp_id;
	private String mem_id;
	private String cp_id;
	private String mem_cp_start;
	private String mem_cp_end;
	private String mem_cp_status;
	private String mem_cp_record;	
}

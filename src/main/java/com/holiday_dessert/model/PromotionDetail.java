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
public class PromotionDetail {

	private String pmd_id;
	private String pd_id;
	private String pm_id;
	private String pmd_start;
	private String pmd_end;
	private String pmd_pd_discount_price;
}

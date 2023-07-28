package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class PromotionDetail {

	private String pmdId;              // 活動優惠明細ID
	private String pdId;               // 商品ID
	private String pmId;               // 活動優惠ID
	private String pmdStart;           // 起始時間
	private String pmdEnd;             // 截止時間
	private String pmdPdDiscountPrice; // 商品活動優惠價
}

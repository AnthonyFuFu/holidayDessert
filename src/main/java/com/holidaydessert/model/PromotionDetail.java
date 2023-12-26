package com.holidaydessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NonNull
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetail extends Base {

	private String pmdId;              // 活動優惠明細ID
	private String pdId;               // 商品ID
	private String pmId;               // 活動優惠ID
	private String pmdStart;           // 起始時間
	private String pmdEnd;             // 截止時間
	private String pmdPdDiscountPrice; // 商品活動優惠價

}

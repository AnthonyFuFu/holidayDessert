package com.holidaydessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@NonNull
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail extends Base {
	
	private String ordId;              // 訂單ID
	private String pdId;               // 商品ID
	private String orddNumber;         // 商品個數
	private String orddPrice;          // 商品單價
	private String orddDiscountPrice;  // 商品活動優惠單價
	
}
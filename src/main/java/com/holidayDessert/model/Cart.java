package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NonNull
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

	private String memId;              // 會員ID
	private String pdId;               // 商品ID
	private String cartPdQuantity;     // 商品個數
	
}
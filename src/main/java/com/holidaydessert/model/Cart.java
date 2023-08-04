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
public class Cart extends Base {

	private String memId;              // 會員ID
	private String pdId;               // 商品ID
	private String cartPdQuantity;     // 商品個數
	
}
package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class Product {

	private String pdId;               // 商品ID
	private String pdcId;              // 商品分類ID
	private String pdName;             // 商品名稱
	private String pdPrice;            // 商品原價
	private String pdDescription;      // 商品資訊
	private String pdStatus;           // 商品狀態(0:下架 1:上架 Default:0)
	
}
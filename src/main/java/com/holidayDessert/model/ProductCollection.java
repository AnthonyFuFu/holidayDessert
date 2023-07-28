package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class ProductCollection {
	
	private String pdcId;              // 商品分類ID
	private String pdcName;            // 商品分類名稱
}

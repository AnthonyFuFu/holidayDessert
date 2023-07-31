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
public class ProductCollection {
	
	private String pdcId;              // 商品分類ID
	private String pdcName;            // 商品分類名稱
}

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
public class ProductCollection extends Base {
	
	private String pdcId;              // 商品分類ID
	private String pdcName;            // 商品分類名稱
	private String pdcKeyword;         // 商品分類關鍵字
	private String pdcStatus;		   // 商品分類狀態(0:下架 1:上架 Default:0)
	
}

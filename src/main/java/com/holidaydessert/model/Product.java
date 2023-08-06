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
public class Product extends Base {

	private String pdId;               // 商品ID
	private String pdcId;              // 商品分類ID
	private String pdName;             // 商品名稱
	private String pdPrice;            // 商品原價
	private String pdDisplayQuantity;  // 商品圖片陳列數量
	private String pdDescription;      // 商品資訊
	private String pdStatus;           // 商品狀態(0:下架 1:上架 Default:0)
	private String pdIsDel;            // 商品刪除(0:未刪除 1:刪除 Default:0)
	private String pdCreateBy;         // 商品創建人
	private String pdCreateTime;       // 商品創建時間
	private String pdUpdateBy;         // 商品最後修改人
	private String pdUpdateTime;       // 商品最後修改時間
	
}
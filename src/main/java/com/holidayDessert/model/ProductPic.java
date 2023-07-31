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
public class ProductPic {

	private String pdPicId;            // 商品圖片ID
	private String pdId;               // 商品ID
	private byte[] pdPic;              // 商品圖片
}

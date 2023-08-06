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
public class ProductPic extends Base {

	private String pdPicId;            // 商品圖片ID
	private String pdId;               // 商品ID
	private String pdPicSort;          // 商品圖片排序
	private byte[] pdPic;              // 商品圖片
	
}

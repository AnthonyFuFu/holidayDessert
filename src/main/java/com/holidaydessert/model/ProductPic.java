package com.holidaydessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NonNull
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductPic extends Base {

	private String pdPicId;            // 商品圖片ID
	private String pdId;               // 商品ID
	private String pdPicSort;          // 商品圖片排序
	private String pdPicture;          // 商品圖片路徑
	private String pdImage;            // 商品圖片名稱

	private String pdName;             // 商品名稱
}

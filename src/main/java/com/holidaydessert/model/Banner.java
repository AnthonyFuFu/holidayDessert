package com.holidaydessert.model;

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
public class Banner {

	private String banId;              // 公告BannerID
	private String newsId;             // 公告ID
	private String banPicture;         // 公告Banner圖片路徑
	private String banImage;           // 公告Banner圖片名稱

	private String newsName;           // 最新消息名稱
	
}

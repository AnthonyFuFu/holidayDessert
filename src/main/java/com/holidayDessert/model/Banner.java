package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class Banner {

	private String banId;              // 公告BannerID
	private String newsId;             // 公告ID
	private byte[] banPic;             // 公告Banner
	
}

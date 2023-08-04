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
	private byte[] banPic;             // 公告Banner
	
}

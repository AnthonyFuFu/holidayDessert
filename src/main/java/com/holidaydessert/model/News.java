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
public class News {

	private String newsId;             // 公告ID
	private String newsContent;        // 公告內容
	private String newsStatus;         // 公告狀態(0:下架 1:上架 Default:0)
	private String newsTime;           // 發布時間
	private String newsName;           // 公告名稱

}

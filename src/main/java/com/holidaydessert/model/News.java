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
public class News extends Base {

	private String newsId;             // 公告ID
	private String newsName;           // 公告名稱
	private String newsContent;        // 公告內容
	private String newsStatus;         // 公告狀態(0:下架 1:上架 Default:0)
	private String newsStart;		   // 開始時間
	private String newsEnd;			   // 結束時間
	private String newsCreate;		   // 發布時間
	
}

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

	private String newsId;             // 最新消息ID
	private String pmId;               // 活動優惠ID
	private String newsName;           // 最新消息名稱
	private String newsContent;        // 最新消息內容
	private String newsStatus;         // 最新消息狀態(0:下架 1:上架 Default:0)
	private String newsStart;		   // 開始時間
	private String newsEnd;			   // 結束時間
	private String newsCreate;		   // 發布時間
	
}

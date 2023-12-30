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
public class ChatRoom {
	
	private String roomId;			   // 聊天室ID
	private String roomUrl;			   // 聊天室URL
	private String roomStatus;		   // 聊天室狀態
	private String roomUpdateStatus;   // 聊天室已讀狀態
	private String roomLastUpdate;	   // 聊天室最後更新時間
	
}

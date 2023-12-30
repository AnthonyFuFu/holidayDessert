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
public class Message {

	private String msgId;              // 聊天紀錄ID
	private String empId;              // 管理員ID
	private String memId;              // 會員ID
	private String roomId;			   // 聊天室ID
	private String msgContent;         // 聊天內容
	private String msgTime;            // 訊息時間
	private String msgDirection;       // 發送方向(0:客服對會員 1:會員對客服)
	private String msgPicture;         // 聊天圖片路徑
	private String msgImage;           // 聊天圖片名稱
	
}

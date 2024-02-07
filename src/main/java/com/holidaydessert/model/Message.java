package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Entity
@Table(name = "message")
public class Message {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MSG_ID")
	private String msgId;              // 聊天紀錄ID
    
    @Column(name = "EMP_ID")
	private String empId;              // 管理員ID
    
    @Column(name = "MEM_ID")
	private String memId;              // 會員ID
    
    @Column(name = "ROOM_ID")
	private String roomId;			   // 聊天室ID
    
    @Column(name = "MSG_CONTENT")
	private String msgContent;         // 聊天內容
    
    @Column(name = "MSG_TIME")
	private String msgTime;            // 訊息時間
    
    @Column(name = "MSG_DIRECTION")
	private String msgDirection;       // 發送方向(0:客服對會員 1:會員對客服)
    
    @Column(name = "MSG_PICTURE")
	private String msgPicture;         // 聊天圖片路徑
    
    @Column(name = "MSG_IMAGE")
	private String msgImage;           // 聊天圖片名稱
    
    @ManyToOne
    @JoinColumn(name = "EMP_ID", insertable = false, updatable = false)
	private Employee employee;         // 管理員
    
    @ManyToOne
    @JoinColumn(name = "MEM_ID", insertable = false, updatable = false)
	private Member member;             // 會員
    
    @ManyToOne
    @JoinColumn(name = "ROOM_ID", insertable = false, updatable = false)
	private ChatRoom chatRoom;		   // 聊天室
    
}

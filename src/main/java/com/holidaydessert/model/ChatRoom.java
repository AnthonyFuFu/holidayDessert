package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "chat_room")
public class ChatRoom {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
	private String roomId;			   // 聊天室ID
    
    @Column(name = "ROOM_URL")
	private String roomUrl;			   // 聊天室URL
    
    @Column(name = "ROOM_STATUS")
	private String roomStatus;		   // 聊天室狀態
    
    @Column(name = "ROOM_UPDATE_STATUS")
	private String roomUpdateStatus;   // 聊天室已讀狀態
    
    @Column(name = "ROOM_LAST_UPDATE")
	private String roomLastUpdate;	   // 聊天室最後更新時間
    
}
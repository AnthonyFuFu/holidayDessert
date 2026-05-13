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
	private Long roomId;			   // 聊天室ID
    
    @Column(name = "ROOM_URL")
	private String roomUrl;			   // 聊天室URL
    
    @Column(name = "ROOM_STATUS")
	private Integer roomStatus;		   // 聊天室狀態
    
    @Column(name = "ROOM_UPDATE_STATUS")
	private Integer roomUpdateStatus;   // 聊天室已讀狀態

    // DEFAULT NOW() 或 @CreationTimestamp 自動產生
    @Column(name = "ROOM_LAST_UPDATE", insertable = false, updatable = false)
	private String roomLastUpdate;	   // 聊天室最後更新時間
    
    // 靜態工廠方法：固定初始值集中管理
    public static ChatRoom createNew(String roomUrl) {
        return ChatRoom.builder()
                .roomUrl(roomUrl)
                .roomStatus(1)       // 初始狀態固定為 1
                .roomUpdateStatus(0) // 初始更新狀態固定為 0
                .build();
    }
    
}
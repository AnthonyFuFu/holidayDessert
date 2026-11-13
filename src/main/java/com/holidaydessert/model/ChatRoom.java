package com.holidaydessert.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
@Table(name = "chat_room", schema = "holiday_dessert")
public class ChatRoom {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
	private Long roomId;			   // 聊天室ID
    
    @Column(name = "MEM_ID", nullable = false, unique = true)
	private Integer memId;			   // 會員ID（一個會員只有一間聊天室）
    
    @Column(name = "EMP_ID")
	private Integer empId;			   // 負責客服ID（NULL 代表尚未認領）
    
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
    public static ChatRoom createNew(Integer memId, String roomUrl) {
        return ChatRoom.builder()
                .memId(memId)
                .roomUrl(roomUrl)
                .roomStatus(1)       // 初始狀態固定為 1
                .roomUpdateStatus(0) // 初始更新狀態固定為 0
                .build();
    }
    
}
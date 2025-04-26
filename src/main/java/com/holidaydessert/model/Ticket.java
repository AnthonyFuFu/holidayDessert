package com.holidaydessert.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "ticket")
public class Ticket implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "TICKET_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;  // 票券 ID
    
    @Column(name = "TICKET_EVENT", nullable = false, unique = true)
    private String ticketEvent; // 活動名稱
    
    @Column(name = "TICKET_QUANTITY", nullable = false)
    private Integer ticketQuantity; // 剩餘票數
    
    @Column(name = "TICKET_PRICE", nullable = false)
    private Integer ticketPrice; // 價格
    
    @Column(name = "TICKET_CREATE_TIME", updatable = false, insertable = false)
    private String ticketCreateTime; // 建立時間（自動生成）
    
    @Column(name = "TICKET_UPDATE_TIME", insertable = false)
    private String ticketUpdateTime; // 更新時間（自動更新）
    
}

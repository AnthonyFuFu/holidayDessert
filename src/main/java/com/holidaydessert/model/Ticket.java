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
    private Integer ticketId;

    @Column(name = "TICKET_NAME", nullable = false, unique = true)
    private String ticketName;

    @Column(name = "TICKET_START", nullable = false)
    private String ticketStart;

    @Column(name = "TICKET_SALE_START", nullable = false)
    private String ticketSaleStart;

    @Column(name = "TICKET_SALE_END", nullable = false)
    private String ticketSaleEnd;

    @Column(name = "TICKET_QUANTITY", nullable = false)
    private Integer ticketQuantity;

    @Column(name = "TICKET_STATUS", nullable = false)
    private Integer ticketStatus;

    @Column(name = "TICKET_CREATE_TIME", updatable = false, insertable = false)
    private String ticketCreateTime; // 建立時間（自動生成）

    @Column(name = "TICKET_UPDATE_TIME", insertable = false)
    private String ticketUpdateTime; // 更新時間（自動更新）
    
}

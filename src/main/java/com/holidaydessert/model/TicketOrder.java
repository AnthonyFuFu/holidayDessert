package com.holidaydessert.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "ticket_order")
public class TicketOrder implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "TICKET_ORD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketOrdId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private TicketType ticketType;

    @Column(name = "TICKET_ORD_QUANTITY", nullable = false)
    private Integer ticketOrdQuantity;

    @Column(name = "TICKET_ORD_STATUS", nullable = false)
    private Integer ticketOrdStatus;

    @Column(name = "TICKET_ORD_TIME", updatable = false, insertable = false)
    private LocalDateTime ticketOrdTime;
    
    @Column(name = "TICKET_ORD_AMOUNT", nullable = false)
    private Integer ticketOrdAmount;         // 實際付款金額快照

    @Column(name = "TICKET_ORD_EXPIRE")
    private LocalDateTime ticketOrdExpire;   // 付款截止時間

    @Column(name = "TICKET_ORD_PAY_TIME")
    private LocalDateTime ticketOrdPayTime;  // 付款時間
}

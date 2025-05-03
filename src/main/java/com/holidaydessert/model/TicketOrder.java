package com.holidaydessert.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @ManyToOne
    @JoinColumn(name = "MEM_ID", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private TicketType ticketType;

    @Column(name = "TICKET_ORD_QUANTITY", nullable = false)
    private Integer ticketOrdQuantity;

    @Column(name = "TICKET_ORD_STATUS", nullable = false)
    private Integer ticketOrdStatus;

    @Column(name = "TICKET_ORD_TIME", updatable = false, insertable = false)
    private String ticketOrdTime;
}

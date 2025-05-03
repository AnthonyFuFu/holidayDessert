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
@Table(name = "ticket_type")
public class TicketType implements Serializable  {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TYPE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    @ManyToOne
    @JoinColumn(name = "TICKET_ID", nullable = false)
    private Ticket ticket;

    @Column(name = "TYPE_NAME", nullable = false)
    private String typeName;

    @Column(name = "TYPE_PRICE", nullable = false)
    private Integer typePrice;

    @Column(name = "TYPE_QUANTITY", nullable = false)
    private Integer typeQuantity;

    @Column(name = "TYPE_CREATE_TIME", updatable = false, insertable = false)
    private String typeCreateTime;

    @Column(name = "TYPE_UPDATE_TIME", insertable = false)
    private String typeUpdateTime;
}

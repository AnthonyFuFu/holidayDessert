package com.holidaydessert.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

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
    
    @Column(name = "TYPE_REMAINING", nullable = false)
    private Integer typeRemaining;           // 剩餘票數

    @Column(name = "TYPE_MAX_PER_PERSON", nullable = false)
    private Integer typeMaxPerPerson;        // 每人購買上限

    @Version                                 // 樂觀鎖，JPA 自動管理
    @Column(name = "LOCK_VERSION", nullable = false)
    private Integer lockVersion;             // 樂觀鎖版本號
    
    @Column(name = "TYPE_CREATE_TIME", updatable = false, insertable = false)
    private LocalDateTime typeCreateTime;

    @Column(name = "TYPE_UPDATE_TIME", insertable = false)
    private LocalDateTime typeUpdateTime;
}

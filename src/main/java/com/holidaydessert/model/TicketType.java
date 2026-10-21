package com.holidaydessert.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import lombok.Data;

@Entity
@Data
@Table(name = "ticket_type", schema = "holiday_dessert")
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

    @Column(name = "TYPE_AREA")
    private String typeArea;                 // 區域群組（座位圖分層用）

    @Column(name = "TYPE_COLOR")
    private String typeColor;                // 座位圖顏色

    @Column(name = "TYPE_ROWS")
    private Integer typeRows;                // 排數（null 代表自由座，不配位）

    @Column(name = "TYPE_SEATS_PER_ROW")
    private Integer typeSeatsPerRow;         // 每排座位數

    @Column(name = "TYPE_SORT", nullable = false)
    private Integer typeSort = 0;            // 排序

    /** 是否為劃位票種（有排數、每排座位數才會配位） */
    public boolean hasSeatMap() {
        return typeRows != null && typeRows > 0 && typeSeatsPerRow != null && typeSeatsPerRow > 0;
    }

    @Version                                 // 樂觀鎖，JPA 自動管理
    @Column(name = "LOCK_VERSION", nullable = false)
    private Integer lockVersion;             // 樂觀鎖版本號
    
    @Column(name = "TYPE_CREATE_TIME", updatable = false, insertable = false)
    private LocalDateTime typeCreateTime;

    @Column(name = "TYPE_UPDATE_TIME", insertable = false)
    private LocalDateTime typeUpdateTime;
}

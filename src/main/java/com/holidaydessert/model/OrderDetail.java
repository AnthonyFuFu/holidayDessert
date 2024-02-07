package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
@NonNull
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail extends Base {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORD_ID")
	private String ordId;              // 訂單ID
    
    @Column(name = "PD_ID")
	private String pdId;               // 商品ID
    
    @Column(name = "ORDD_NUMBER")
	private String orddNumber;         // 商品個數
    
    @Column(name = "ORDD_PRICE")
	private String orddPrice;          // 商品單價
    
    @Column(name = "ORDD_DISCOUNT_PRICE")
	private String orddDiscountPrice;  // 商品活動優惠單價
    
    @ManyToOne
    @JoinColumn(name = "PD_ID", insertable = false, updatable = false)
    private Product product;     // 部門
    
}
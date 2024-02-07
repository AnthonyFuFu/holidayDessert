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
import lombok.ToString;

@Getter
@Setter
@Builder
@NonNull
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promotion_detail")
public class PromotionDetail extends Base {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PMD_ID")
	private String pmdId;              // 活動優惠明細ID
    
    @Column(name = "PD_ID")
	private String pdId;               // 商品ID
    
    @Column(name = "PM_ID")
	private String pmId;               // 活動優惠ID
    
    @Column(name = "PMD_START")
	private String pmdStart;           // 起始時間
    
    @Column(name = "PMD_END")
	private String pmdEnd;             // 截止時間
    
    @Column(name = "PMD_PD_DISCOUNT_PRICE")
	private String pmdPdDiscountPrice; // 商品活動優惠價
    
    @ManyToOne
    @JoinColumn(name = "PD_ID", insertable = false, updatable = false)
    private Product product;		   // 商品
    
    @ManyToOne
    @JoinColumn(name = "PM_ID", insertable = false, updatable = false)
    private Promotion promotion;	   // 活動優惠
    
}

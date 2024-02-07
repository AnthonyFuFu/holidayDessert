package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "product_collection")
public class ProductCollection extends Base {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PDC_ID")
	private String pdcId;              // 商品分類ID
    
    @Column(name = "PDC_NAME")
	private String pdcName;            // 商品分類名稱
    
    @Column(name = "PDC_KEYWORD")
	private String pdcKeyword;         // 商品分類關鍵字
    
    @Column(name = "PDC_STATUS")
	private String pdcStatus;		   // 商品分類狀態(0:下架 1:上架 Default:0)
	
}

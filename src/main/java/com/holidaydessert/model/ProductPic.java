package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "product_pic")
public class ProductPic extends Base {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PD_PIC_ID")
	private String pdPicId;            // 商品圖片ID
    
    @Column(name = "PD_ID")
	private String pdId;               // 商品ID
    
    @Column(name = "PD_PIC_SORT")
	private String pdPicSort;          // 商品圖片排序
    
    @Column(name = "PD_PICTURE")
	private String pdPicture;          // 商品圖片路徑
    
    @Column(name = "PD_IMAGE")
	private String pdImage;            // 商品圖片名稱
    
	@Transient
	private String pdName;             // 商品名稱
	
    @ManyToOne
    @JoinColumn(name = "PD_ID", insertable = false, updatable = false)
    private Product product;		   // 商品
    
}

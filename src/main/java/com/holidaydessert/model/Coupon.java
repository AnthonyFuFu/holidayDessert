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
@Table(name = "coupon")
public class Coupon extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CP_ID")
	private String cpId;               // 優惠券種類ID
    
    @Column(name = "CP_NAME")
	private String cpName;             // 優惠券名稱
    
    @Column(name = "CP_DISCOUNT")
	private String cpDiscount;         // 優惠券折扣價格
    
    @Column(name = "CP_STATUS")
	private String cpStatus;           // 優惠券狀態(0:下架 1:上架)
    
    @Column(name = "CP_PICTURE")
	private String cpPicture;          // 優惠券圖片路徑
    
    @Column(name = "CP_IMAGE")
	private String cpImage;            // 優惠券圖片名稱

}

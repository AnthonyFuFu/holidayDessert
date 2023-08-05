package com.holidaydessert.model;

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
public class Coupon extends Base {

	private String cpId;               // 優惠券種類ID
	private String cpName;             // 優惠券名稱
	private String cpDiscount;         // 優惠券折扣價格
	private String cpStatus;           // 優惠券狀態(0:下架 1:上架)
	private byte[] cpPic;              // 優惠券圖片

}

package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class Coupon {

	private String cpId;               // 優惠券種類ID
	private String cpName;             // 優惠券名稱
	private String cpDiscount;         // 優惠券折扣價格
	private String cpStatus;           // 優惠券狀態(0:不可用 1:可用)
	private byte[] cpPic;              // 優惠券圖片

}

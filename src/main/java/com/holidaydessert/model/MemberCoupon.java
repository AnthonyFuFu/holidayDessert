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
public class MemberCoupon extends Base {

	private String memCpId;            // 會員優惠券ID
	private String memId;              // 會員ID
	private String cpId;               // 優惠券種類ID
	private String memCpStart;         // 起始時間
	private String memCpEnd;           // 截止時間
	private String memCpStatus;        // 使用狀態(0:不可用 1:可用)
	private String memCpRecord;	       // 使用紀錄
	
}

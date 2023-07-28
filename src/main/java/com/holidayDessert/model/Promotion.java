package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class Promotion {

	private String pmId;               // 活動優惠ID
	private String pmName;             // 活動名稱
	private String pmDescription;      // 活動描述
	private String pmDiscount;         // 折扣幅度
	private String pmStatus;           // 活動狀態(0:不可用 1:可用)
}
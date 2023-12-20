package com.holidaydessert.model;

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
public class Promotion extends Base {

	private String pmId;               // 活動優惠ID
	private String pmName;             // 活動名稱
	private String pmDescription;      // 活動描述
	private String pmDiscount;         // 折扣幅度
	private String pmRegularly;		   // 活動例行性(0:非例行 1:例行)
	private String pmStatus;           // 活動狀態(0:下架 1:上架)
	private String pmStart;			   // 開始時間
	private String pmEnd;			   // 結束時間

}
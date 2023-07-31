package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NonNull
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInformation {

	private String comId;              // 公司資訊ID
	private String comName;            // 店鋪名稱
	private String comAddress;         // 店鋪地址
	private String comPhone;           // 店鋪電話
	private String comMemo;            // 店鋪詳細資訊
	
}
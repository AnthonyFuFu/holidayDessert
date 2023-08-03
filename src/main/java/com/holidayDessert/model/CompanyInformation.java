package com.holidayDessert.model;

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
public class CompanyInformation extends Base {

	private String comId;              // 公司資訊ID
	private String comName;            // 店鋪名稱
	private String comAddress;         // 店鋪地址
	private String comPhone;           // 店鋪電話
	private String comMemo;            // 店鋪詳細資訊
	
}
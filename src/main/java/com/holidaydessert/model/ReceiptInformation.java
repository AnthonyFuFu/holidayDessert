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
public class ReceiptInformation extends Base{
	
	private String rcpId;              // 常用收貨資訊ID
	private String memId;              // 會員ID
	private String rcpName;            // 收貨者姓名
	private String rcpCvs;             // 超商收貨地址
	private String rcpAddress;         // 收件人地址
	private String rcpPhone;           // 收件人聯絡電話
	
}
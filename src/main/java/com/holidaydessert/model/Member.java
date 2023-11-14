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
public class Member extends Base {
	
	private String memId;              // 會員ID
	private String memName;            // 姓名
	private String memAccount;         // 帳號
	private String memPassword;        // 密碼
	private String memGender;          // 性別(f:女 m:男)
	private String memPhone;           // 電話
	private String memEmail;           // 信箱
	private String memAddress;         // 地址
	private String memBirthday;        // 生日
	private String memStatus;          // 會員狀態(1:正常 0:停權)
	private String memVerificationStatus;// 會員驗證狀態(1:開通 0:停權)
	private String memVerificationCode;// 會員驗證碼
	private String memGoogleUid;	   // 會員googleUid

	private String totalExpense;	   // 總金額
	
}
package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class Employee {
	
	private String empId;              // 管理員ID
	private String empName;            // 姓名
	private String empPhone;           // 電話
	private byte[] empPicture;         // 照片
	private String empAccount;         // 帳號
	private String empPassword;        // 密碼
	private String empLevel;           // 等級(0:最高管理員 1:一般管理員)
	private String empStatus;          // 狀態(0:啟用 1:停權)
	private String empHiredate;        // 入職日
	
}
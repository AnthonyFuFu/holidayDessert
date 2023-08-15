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
public class Employee extends Base {
	
	private String empId;              // 管理員ID
	private String deptId;             // 部門ID
	private String empName;            // 姓名
	private String empPhone;           // 電話
	private String empJob;			   // 職稱
	private String empSalary;		   // 薪水
	private String empPicture;         // 照片路徑
	private String empAccount;         // 帳號
	private String empPassword;        // 密碼
	private String empEmail;           // 信箱
	private String empLevel;           // 等級(0:最高管理員 1:一般管理員)
	private String empStatus;          // 狀態(0:停權 1:啟用)
	private String empHiredate;        // 入職日
	
}
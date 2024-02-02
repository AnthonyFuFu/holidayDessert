package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

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
@Entity
public class Member extends Base {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEM_ID")
	private String memId;              // 會員ID
	@Column(name = "MEM_NAME")
	private String memName;            // 姓名
	@Column(name = "MEM_ACCOUNT")
	private String memAccount;         // 帳號
	@Column(name = "MEM_PASSWORD")
	private String memPassword;        // 密碼
	@Column(name = "MEM_GENDER")
	private String memGender;          // 性別(f:女 m:男)
	@Column(name = "MEM_PHONE")
	private String memPhone;           // 電話
	@Column(name = "MEM_EMAIL")
	private String memEmail;           // 信箱
	@Column(name = "MEM_ADDRESS")
	private String memAddress;         // 地址
	@Column(name = "MEM_BIRTHDAY")
	private String memBirthday;        // 生日
	@Column(name = "MEM_STATUS")
	private String memStatus;          // 會員狀態(1:正常 0:停權)
	@Column(name = "MEM_VERIFICATION_STATUS")
	private String memVerificationStatus;// 會員驗證狀態(1:開通 0:停權)
	@Column(name = "MEM_VERIFICATION_CODE")
	private String memVerificationCode;// 會員驗證碼
	@Column(name = "MEM_GOOGLE_UID")
	private String memGoogleUid;	   // 會員googleUid
	
	@Transient
	private String totalExpense;	   // 總金額
	
}
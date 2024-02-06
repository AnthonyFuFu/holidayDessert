package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "company_information")
public class CompanyInformation extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COM_ID")
	private String comId;              // 公司資訊ID
    
    @Column(name = "COM_NAME")
	private String comName;            // 店鋪名稱
    
    @Column(name = "COM_ADDRESS")
	private String comAddress;         // 店鋪地址
    
    @Column(name = "COM_PHONE")
	private String comPhone;           // 店鋪電話
    
    @Column(name = "COM_MEMO")
	private String comMemo;            // 店鋪詳細資訊
	
}
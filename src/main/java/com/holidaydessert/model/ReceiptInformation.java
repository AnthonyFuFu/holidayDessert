package com.holidaydessert.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
@Table(name = "receipt_information")
public class ReceiptInformation extends Base{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RCP_ID")
	private String rcpId;              // 常用收貨資訊ID
    
    @Column(name = "MEM_ID")
	private String memId;              // 會員ID
    
    @Column(name = "RCP_NAME")
	private String rcpName;            // 收貨者姓名
    
    @Column(name = "RCP_CVS")
	private String rcpCvs;             // 超商收貨地址
    
    @Column(name = "RCP_ADDRESS")
	private String rcpAddress;         // 收件人地址
    
    @Column(name = "RCP_PHONE")
	private String rcpPhone;           // 收件人聯絡電話
    
    @ManyToOne
    @JoinColumn(name = "MEM_ID", insertable = false, updatable = false)
    private Member member;			   // 會員
    
}
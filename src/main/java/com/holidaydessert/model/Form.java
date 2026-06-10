package com.holidaydessert.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
@Table(name = "form")
public class Form extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FORM_ID")
	private String formId;		  	   // 送單ID

    @Column(name = "FORM_PHONE")
	private String formPhone;	  	   // 送單人電話

    @Column(name = "FORM_EMAIL")
	private String formEmail;	  	   // 送單人信箱

    @Column(name = "FORM_CONTENT")
	private String formContent;	  	   // 送單內容

    @Column(name = "FORM_CREATE_BY")
	private String formCreateBy;  	   // 送單人
    
    @Column(name = "FORM_CREATE_TIME", insertable = false, updatable = false)
	private String formCreateTime; 	   // 送單創建時間
	
}

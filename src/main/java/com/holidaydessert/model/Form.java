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

    @Column(name = "FORM_CREATE_TIME")
	private String formCreateTime; 	   // 送單創建時間
	
}

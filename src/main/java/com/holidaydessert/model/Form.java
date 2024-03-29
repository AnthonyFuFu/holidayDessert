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
	private String formId;

    @Column(name = "FORM_PHONE")
	private String formPhone;

    @Column(name = "FORM_EMAIL")
	private String formEmail;

    @Column(name = "FORM_CONTENT")
	private String formContent;

    @Column(name = "FORM_CREATE_BY")
	private String formCreateBy;

    @Column(name = "FORM_CREATE_TIME")
	private String formCreateTime;
	
}

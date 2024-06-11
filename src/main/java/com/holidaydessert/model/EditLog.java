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
@Table(name = "edit_log")
public class EditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
	private String logId;			   // 操作紀錄ID
    
    @Column(name = "LOG_IP")
	private String logIp;			   // 操作紀錄IP
    
    @Column(name = "LOG_URL")
	private String logUrl;			   // 操作紀錄URL
    
    @Column(name = "LOG_TYPE")
	private String logType;			   // 操作紀錄前/後台
    
    @Column(name = "LOG_METHOD")
	private String logMethod;		   // 操作紀錄請求方法
    
    @Column(name = "LOG_HTTP_STATUS_CODE")
	private String logHttpStatusCode;  // 操作紀錄狀態碼
    
    @Column(name = "LOG_CONTENT")
	private String logContent;		   // 操作紀錄內容
    
    @Column(name = "LOG_CREATE_BY")
	private String logCreateBy;		   // 操作紀錄創建人
    
    @Column(name = "LOG_CREATE_TIME")
	private String logCreateTime;	   // 操作紀錄創建時間
	
}

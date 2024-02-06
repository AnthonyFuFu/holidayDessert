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
@Table(name = "promotion")
public class Promotion extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PM_ID")
	private String pmId;               // 活動優惠ID
    
    @Column(name = "PM_NAME")
	private String pmName;             // 活動名稱
    
    @Column(name = "PM_DESCRIPTION")
	private String pmDescription;      // 活動描述
    
    @Column(name = "PM_DISCOUNT")
	private String pmDiscount;         // 折扣幅度
    
    @Column(name = "PM_REGULARLY")
	private String pmRegularly;		   // 活動例行性(0:非例行 1:例行)
    
    @Column(name = "PM_STATUS")
	private String pmStatus;           // 活動狀態(0:下架 1:上架)
    
    @Column(name = "PM_START")
	private String pmStart;			   // 開始時間
    
    @Column(name = "PM_END")
	private String pmEnd;			   // 結束時間

}
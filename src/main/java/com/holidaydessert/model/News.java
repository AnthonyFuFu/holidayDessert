package com.holidaydessert.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "news")
public class News extends Base {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NEWS_ID")
	private String newsId;             // 最新消息ID
    
    @Column(name = "PM_ID")
	private String pmId;               // 活動優惠ID
    
    @Column(name = "NEWS_NAME")
	private String newsName;           // 最新消息名稱
    
    @Column(name = "NEWS_CONTENT")
	private String newsContent;        // 最新消息內容
    
    @Column(name = "NEWS_STATUS")
	private String newsStatus;         // 最新消息狀態(0:下架 1:上架 Default:0)
    
    @Column(name = "NEWS_START")
	private String newsStart;		   // 開始時間
    
    @Column(name = "NEWS_END")
	private String newsEnd;			   // 結束時間
    
    @Column(name = "NEWS_CREATE")
	private String newsCreate;		   // 發布時間
    
	@Transient
	private List<Map<String, Object>> bannerList;
	
    @ManyToOne
    @JoinColumn(name = "PM_ID", insertable = false, updatable = false)
	private Promotion promotion;       // 活動優惠
    
}

package com.holidaydessert.model;

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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NonNull
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banner")
public class Banner {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BAN_ID")
	private String banId;              // 公告BannerID
    
    @Column(name = "NEWS_ID")
	private String newsId;             // 最新消息ID
    
    @Column(name = "BAN_PICTURE")
	private String banPicture;         // 公告Banner圖片路徑
    
    @Column(name = "BAN_IMAGE")
	private String banImage;           // 公告Banner圖片名稱
    
	@Transient
	private String newsName;           // 最新消息名稱
	
    @ManyToOne
    @JoinColumn(name = "NEWS_ID", insertable = false, updatable = false)
	private News news;				   // 最新消息
    
}

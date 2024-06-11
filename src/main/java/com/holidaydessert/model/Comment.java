package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CMT_ID")
	private String cmtId;		  	   // 留言ID

    @Column(name = "MEM_ID")
	private String memId;              // 留言人ID

    @Column(name = "CMT_STATUS")
	private String cmtStatus;		   // 是否顯示留言

    @Column(name = "CMT_CHECK")
	private String cmtCheck;		   // 留言是否被確認

    @Column(name = "CMT_PICTURE")
	private String cmtPicture;		   // 留言人圖片

    @Column(name = "CMT_CONTENT")
	private String cmtContent;		   // 留言內容

    @Column(name = "CMT_CREATE_BY")
	private String cmtCreateBy;		   // 留言創建人

    @Column(name = "CMT_CREATE_TIME")
	private String cmtCreateTime;	   // 留言創建時間

    @ManyToOne
    @JoinColumn(name = "MEM_ID", insertable = false, updatable = false)
	private Member member;             // 會員
    
}

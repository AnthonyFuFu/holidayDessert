package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "fullcalendar")
public class Fullcalendar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id; // 唯一的事件 ID

	@Column(name = "title")
	private String title; // 事件的標題

	@Column(name = "start")
	private String start; // 事件的開始日期/時間

	@Column(name = "end")
	private String end; // 事件的結束日期/時間

	@Column(name = "allDay")
	private String allDay; // 是否為全天事件 (布林值)

	@Column(name = "url")
	private String url; // 點擊事件後開啟的連結

	@Column(name = "classNames")
	private String classNames; // 自訂的 CSS 類別名稱 (JSON 格式)

	@Column(name = "editable")
	private String editable; // 是否允許該事件被拖動或調整大小

	@Column(name = "startEditable")
	private String startEditable; // 是否允許調整事件開始時間

	@Column(name = "durationEditable")
	private String durationEditable; // 是否允許改變事件的持續時間

	@Column(name = "overlap")
	private String overlap; // 是否允許事件重疊

	@Column(name = "display")
	private String display; // 事件的顯示方式 'auto', 'block', 'list-item', 'background', 'inverse-background', 'none'

	@Column(name = "backgroundColor")
	private String backgroundColor; // 背景顏色

	@Column(name = "borderColor")
	private String borderColor; // 邊框顏色

	@Column(name = "textColor")
	private String textColor; // 文字顏色

	@Column(name = "groupId")
	private String groupId; // 群組 ID，用於將多個事件歸為同一群組

	@Column(name = "extendedProps")
	private String extendedProps; // 自訂屬性

	@Column(name = "isApproved", nullable = false)
	private String isApproved; // 是否已審核

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID", nullable = false)
	private Employee employee; // 事件擁有者 (對應 Employee 表)

}
package com.holiday_dessert.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

	private String msg_id;
	private String emp_id;
	private String mem_id;
	private String msg_content;
	private String msg_time;
	private String msg_direction;
	private String msg_pic;
}

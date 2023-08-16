package com.holidaydessert.model;

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
public class Department extends Base {
	
	private String deptId;             // 部門ID
	private String deptName;           // 部門名稱
	private String deptLoc;            // 部門地點

}

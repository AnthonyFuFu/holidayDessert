package com.holidaydessert.model;

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
public class Department {
	
	private String deptId;             // 部門ID
	private String deptName;           // 部門名稱
	private String deptLoc;            // 部門地點

}

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
public class Authority extends Base {

	private String empId;              // 管理員ID
	private String funcId;             // 功能ID
	private String authStatus;         // 權限狀態
	
}

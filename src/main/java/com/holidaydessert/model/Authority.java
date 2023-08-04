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
public class Authority {

	private String empId;              // 管理員ID
	private String funcId;             // 功能ID
	private String authStatus;         // 權限狀態
	
}

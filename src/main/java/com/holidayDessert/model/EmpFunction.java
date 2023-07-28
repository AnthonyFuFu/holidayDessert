package com.holidayDessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@NonNull
@AllArgsConstructor
public class EmpFunction {
	
	private String funcId;             // 功能ID
	private String funcName;           // 功能名稱

}

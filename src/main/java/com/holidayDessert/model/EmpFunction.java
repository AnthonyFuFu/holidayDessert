package com.holidayDessert.model;

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
public class EmpFunction {
	
	private String funcId;             // 功能ID
	private String funcName;           // 功能名稱

}

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
	private String funcLayer;          // 功能層級
	private String funcParentId;       // 功能隸屬
	private String funcLink;           // 功能連結
	private String funcStatus;         // 功能狀態
	private String funcIcon;           // ICON
	
}

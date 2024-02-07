package com.holidaydessert.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "emp_function")
public class EmpFunction extends Base {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FUNC_ID")
	private String funcId;             // 功能ID
    
    @Column(name = "FUNC_NAME")
	private String funcName;           // 功能名稱
    
    @Column(name = "FUNC_LAYER")
	private String funcLayer;          // 功能層級
    
    @Column(name = "FUNC_PARENT_ID")
	private String funcParentId;       // 功能隸屬
    
    @Column(name = "FUNC_LINK")
	private String funcLink;           // 功能連結
    
    @Column(name = "FUNC_STATUS")
	private String funcStatus;         // 功能狀態
    
    @Column(name = "FUNC_ICON")
	private String funcIcon;           // ICON
	
}

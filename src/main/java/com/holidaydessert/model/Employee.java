package com.holidaydessert.model;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "employee")
public class Employee extends Base {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMP_ID")
	private String empId;              // 管理員ID
    
    @Column(name = "DEPT_ID")
	private String deptId;             // 部門ID
    
    @Column(name = "EMP_NAME")
	private String empName;            // 姓名
    
    @Column(name = "EMP_PHONE")
	private String empPhone;           // 電話
    
    @Column(name = "EMP_JOB")
	private String empJob;			   // 職稱
    
    @Column(name = "EMP_SALARY")
	private String empSalary;		   // 薪水
    
    @Column(name = "EMP_PICTURE")
	private String empPicture;         // 照片路徑
    
    @Column(name = "EMP_IMAGE")
	private String empImage;       	   // 照片名稱
    
    @Column(name = "EMP_ACCOUNT")
	private String empAccount;         // 帳號
    
    @Column(name = "EMP_PASSWORD")
	private String empPassword;        // 密碼
    
    @Column(name = "EMP_EMAIL")
	private String empEmail;           // 信箱
    
    @Column(name = "EMP_LEVEL")
	private String empLevel;           // 等級(0:最高管理員 1:一般管理員)

    @Column(name = "EMP_MANAGER_ID")
	private String empManagerId;       // 管理者ID
    
    @Column(name = "EMP_STATUS")
	private String empStatus;          // 狀態(0:停權 1:啟用)
    
    @Column(name = "EMP_HIREDATE")
	private String empHiredate;        // 入職日
    
    @Column(name = "EMP_THEME")
	private String empTheme;		   // 主題
	
	@Transient
	private List<Map<String, Object>> authorityList;
	
    @ManyToOne
    @JoinColumn(name = "DEPT_ID", insertable = false, updatable = false)
    private Department department;     // 部門
    
    @ManyToOne
    @JoinColumn(name = "EMP_ID", insertable = false, updatable = false)
    private Employee employee;     	   // 管理員
    
    @OneToMany(mappedBy = "employee",fetch = FetchType.EAGER)
    private List<Message> messages;
    
}
package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.EmployeeDao;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.repository.EmployeeRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class EmployeeService {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	// =============================================
	// back
	// =============================================
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.EMPLOYEE_TABLE,
				SortFieldsConstant.EMPLOYEE_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> employeePage = employeeRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(employeePage, PageableUtil.toMutableList(employeePage), draw);
	}

	// 權限設定：員工列表 + 每位員工的權限清單（AUTH_LIST）
	public Map<String, Object> authorityList(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.EMPLOYEE_TABLE,
				SortFieldsConstant.EMPLOYEE_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> employeePage = employeeRepository.backList(keyword, pageable);
		// 4. 附上每位員工的權限清單
		List<Map<String, Object>> employeeList = PageableUtil.toMutableList(employeePage);
		for (Map<String, Object> employee : employeeList) {
			Authority authority = new Authority();
			authority.setEmpId(String.valueOf(employee.get("EMP_ID")));
			employee.put("AUTH_LIST", authorityService.getAuthorityList(authority));
		}
		// 5. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(employeePage, employeeList, draw);
	}

	public String getNextId() {
		return employeeDao.getNextId();
	}

	public void add(Employee employee) {
		employeeDao.add(employee);
	}

	public void update(Employee employee) {
		employeeDao.update(employee);
	}

	public void resign(Employee employee) {
		employeeDao.resign(employee);
	}

	public Employee getData(Employee employee) {
		// 查無資料時回傳空的 Employee（Controller 會直接取用欄位）
		return employeeRepository.findDataById(employee.getEmpId()).orElseGet(Employee::new);
	}
	
	public Employee login(Employee employee) {
		return employeeDao.login(employee);
	}

	public void updateTheme(Employee employee) {
		employeeDao.updateTheme(employee);
	}

	public List<Employee> findAllWithDepartment() {
		return employeeRepository.findAllWithDepartment();
	}
	
}

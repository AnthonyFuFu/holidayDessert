package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.DepartmentDao;
import com.holidaydessert.model.Department;
import com.holidaydessert.repository.DepartmentRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DepartmentDao departmentDao;

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
				SortFieldsConstant.DEPARTMENT_TABLE,
				SortFieldsConstant.DEPARTMENT_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> departmentPage = departmentRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(departmentPage, PageableUtil.toMutableList(departmentPage), draw);
	}

	public void add(Department department) {
		departmentDao.add(department);
	}

	public void update(Department department) {
		departmentDao.update(department);
	}

	public void delete(Department department) {
		departmentDao.delete(department);
	}

	public Department getData(Department department) {
		return departmentDao.getData(department);
	}
	
	public List<Map<String, Object>> getList() {
		return departmentDao.getList();
	}

}

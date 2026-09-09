package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.DepartmentDao;
import com.holidaydessert.model.Department;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;
	
	// back
	public List<Map<String, Object>> list(Department department) {
		return departmentDao.list(department);
	}

	public int getCount(Department department) {
		return departmentDao.getCount(department);
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

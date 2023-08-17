package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.DepartmentDao;
import com.holidaydessert.model.Department;
import com.holidaydessert.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Override
	public List<Map<String, Object>> list(Department department) {
		return departmentDao.list(department);
	}

	@Override
	public int getCount(Department department) {
		return departmentDao.getCount(department);
	}
	
	@Override
	public void add(Department department) {
		departmentDao.add(department);
	}

	@Override
	public void update(Department department) {
		departmentDao.update(department);
	}

	@Override
	public void delete(Department department) {
		departmentDao.delete(department);
	}

	@Override
	public List<Map<String, Object>> getList() {
		return departmentDao.getList();
	}

}

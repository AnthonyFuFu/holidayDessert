package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.EmployeeDao;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	@Override
	public List<Map<String, Object>> list(Employee employee) {
		return employeeDao.list(employee);
	}

	@Override
	public int getCount(Employee employee) {
		return employeeDao.getCount(employee);
	}

	@Override
	public void add(Employee employee) {
		employeeDao.add(employee);
	}

	@Override
	public void update(Employee employee) {
		employeeDao.update(employee);
	}

	@Override
	public void resign(Employee employee) {
		employeeDao.resign(employee);
	}

	@Override
	public Employee login(Employee employee) {
		return employeeDao.login(employee);
	}

}

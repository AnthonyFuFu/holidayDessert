package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.EmployeeDao;
import com.holidayDessert.model.Employee;
import com.holidayDessert.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	@Override
	public List<Map<String, Object>> list(Employee employee) {
		return employeeDao.list(employee);
	}

}

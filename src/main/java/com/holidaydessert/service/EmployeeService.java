package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.EmployeeDao;
import com.holidaydessert.model.Employee;
import com.holidaydessert.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Employee employee) {
		return employeeDao.list(employee);
	}

	public int getCount(Employee employee) {
		return employeeDao.getCount(employee);
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
		return employeeDao.getData(employee);
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

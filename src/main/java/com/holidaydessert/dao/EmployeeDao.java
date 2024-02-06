package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Employee;

public interface EmployeeDao {

	// back
	public List<Map<String, Object>> list(Employee employee);
	public int getCount(Employee employee);
	public String getNextId();
	public void add(Employee employee);
	public void update(Employee employee);
	public void resign(Employee employee);
	public Employee getData(Employee employee);
	public Employee login(Employee employee);
	public void updateTheme(Employee employee);

	// front
	public List<Employee> findAllWithDepartment();
	
}

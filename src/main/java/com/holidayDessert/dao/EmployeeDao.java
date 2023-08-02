package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Employee;

public interface EmployeeDao {

	// back
	public List<Map<String, Object>> list(Employee employee);
	public int getCount(Employee employee);
	public void add(Employee employee);
	public void update(Employee employee);
	public void delete(Employee employee);
	
}

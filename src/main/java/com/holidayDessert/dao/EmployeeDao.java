package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Employee;

public interface EmployeeDao {

	public List<Map<String, Object>> list(Employee employee);

}

package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Employee;

public interface EmployeeService {

	public List<Map<String, Object>> list(Employee employee);

}

package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Department;

public interface DepartmentService {
	
	// back
	public List<Map<String, Object>> list(Department department);
	public int getCount(Department department);
	public void add(Department department);
	public void update(Department department);
	public void delete(Department department);
	
}

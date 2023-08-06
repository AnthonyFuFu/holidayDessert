package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;

public interface AuthorityDao {
	
	// back
	public List<Map<String, Object>> list(Authority authority);
	public void batchAdd(Employee employee, List<Map<String, Object>> empFunction);
	public void update(Authority authority);
	
}

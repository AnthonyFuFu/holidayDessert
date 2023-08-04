package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;

public interface AuthorityService {
	
	// back
	public List<Map<String, Object>> list(Authority authority);
	public void batchAdd(Employee employee, String[] empFunction);
	public void update(Authority authority);
	
}

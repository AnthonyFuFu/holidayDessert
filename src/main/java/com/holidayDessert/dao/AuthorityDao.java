package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Authority;
import com.holidayDessert.model.Employee;

public interface AuthorityDao {
	
	// back
	public List<Map<String, Object>> list(Authority authority);
	public void batchAdd(Employee employee, String[] empFunction);
	public void update(Authority authority);
	
}

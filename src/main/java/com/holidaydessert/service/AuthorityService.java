package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;

public interface AuthorityService {
	
	// back
	public List<Map<String, Object>> list(Authority authority);
	public List<Map<String, Object>> getAuthorityList(Authority authority);
	public int getCount(Authority authority);
	public void addAdminAuthority(Employee employee, List<Map<String, Object>> empFunction);
	public void addStaffAuthority(Employee employee, List<Map<String, Object>> empFunction);
	public void update(Authority authority);
	
}

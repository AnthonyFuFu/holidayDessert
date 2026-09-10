package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.AuthorityDao;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;

@Service
public class AuthorityService {
	
	@Autowired
	private AuthorityDao authorityDao;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Authority authority) {
		return authorityDao.list(authority);
	}

	public List<Map<String, Object>> getAuthorityList(Authority authority) {
		return authorityDao.getAuthorityList(authority);
	}
	
	public int getCount(Authority authority) {
		return authorityDao.getCount(authority);
	}

	public void addAdminAuthority(Employee employee, List<Map<String, Object>> empFunction) {
		authorityDao.addAdminAuthority(employee, empFunction);
	}
	
	public void addStaffAuthority(Employee employee, List<Map<String, Object>> empFunction) {
		authorityDao.addStaffAuthority(employee, empFunction);
	}
	
	public void update(Authority authority) {
		authorityDao.update(authority);
	}
	
}

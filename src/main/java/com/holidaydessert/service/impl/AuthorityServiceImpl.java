package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.AuthorityDao;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	@Autowired
	private AuthorityDao authorityDao;

	@Override
	public List<Map<String, Object>> list(Authority authority) {
		return authorityDao.list(authority);
	}

	@Override
	public List<Map<String, Object>> getAuthorityList(Authority authority) {
		return authorityDao.getAuthorityList(authority);
	}
	
	@Override
	public int getCount(Authority authority) {
		return authorityDao.getCount(authority);
	}

	@Override
	public void addAdminAuthority(Employee employee, List<Map<String, Object>> empFunction) {
		authorityDao.addAdminAuthority(employee, empFunction);
	}
	
	@Override
	public void addStaffAuthority(Employee employee, List<Map<String, Object>> empFunction) {
		authorityDao.addStaffAuthority(employee, empFunction);
	}
	
	@Override
	public void update(Authority authority) {
		authorityDao.update(authority);
	}
	
}

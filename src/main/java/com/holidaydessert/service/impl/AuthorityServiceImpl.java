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
	public void batchAdd(Employee employee, String[] empFunction) {
		authorityDao.batchAdd(employee, empFunction);
	}

	@Override
	public void update(Authority authority) {
		authorityDao.update(authority);
	}
	
}

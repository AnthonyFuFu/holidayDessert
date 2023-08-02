package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.AuthorityDao;
import com.holidayDessert.model.Authority;
import com.holidayDessert.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	@Autowired
	private AuthorityDao authorityDao;

	@Override
	public List<Map<String, Object>> list(Authority authority) {
		return authorityDao.list(authority);
	}

	@Override
	public void add(Authority authority) {
		authorityDao.add(authority);
	}

	@Override
	public void update(Authority authority) {
		authorityDao.update(authority);
	}

	@Override
	public void delete(Authority authority) {
		authorityDao.delete(authority);
	}
	

}

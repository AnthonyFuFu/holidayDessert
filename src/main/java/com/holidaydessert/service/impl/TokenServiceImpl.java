package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.TokenDao;
import com.holidaydessert.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenDao tokenDao;
	
	@Override
	public List<Map<String, Object>> getToken(String memEmail,String memPassword) {
		return tokenDao.getToken(memEmail, memPassword);
	}
	
}

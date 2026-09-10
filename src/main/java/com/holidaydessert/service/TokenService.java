package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.TokenDao;

@Service
public class TokenService {

	@Autowired
	private TokenDao tokenDao;
	
	public List<Map<String, Object>> getToken(String memEmail,String memPassword) {
		return tokenDao.getToken(memEmail, memPassword);
	}
	
}

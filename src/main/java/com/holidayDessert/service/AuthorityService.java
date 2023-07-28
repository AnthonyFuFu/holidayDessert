package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Authority;

public interface AuthorityService {
	
	public List<Map<String, Object>> list(Authority authority);
	
}

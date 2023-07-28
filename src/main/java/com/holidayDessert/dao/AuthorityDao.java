package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Authority;

public interface AuthorityDao {
	
	public List<Map<String, Object>> list(Authority authority);
	
}

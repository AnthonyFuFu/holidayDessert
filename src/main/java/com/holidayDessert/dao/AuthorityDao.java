package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Authority;

public interface AuthorityDao {
	
	// back
	public List<Map<String, Object>> list(Authority authority);
	public void add(Authority authority);
	public void update(Authority authority);
	public void delete(Authority authority);
	
}

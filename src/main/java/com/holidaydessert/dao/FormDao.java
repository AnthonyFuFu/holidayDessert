package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Form;

public interface FormDao {

	// back
	public List<Map<String, Object>> list(Form form);
	public int getCount(Form form);
	
	// front
	public void add(Form form);
	
}

package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Form;

public interface FormService {

	// back
	public List<Map<String, Object>> list(Form form);
	public int getCount(Form form);
	
	// front
	public void add(Form form);
	
}

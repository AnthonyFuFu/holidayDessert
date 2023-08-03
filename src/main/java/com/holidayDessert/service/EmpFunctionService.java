package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.EmpFunction;

public interface EmpFunctionService {
	
	// back
	public List<Map<String, Object>> list(EmpFunction empFunction);
	public void add(EmpFunction empFunction);
	public void update(EmpFunction empFunction);
	public List<Map<String, Object>> getIdToAuth(EmpFunction empFunction);
	
}

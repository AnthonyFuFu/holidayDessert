package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.EmpFunction;

public interface EmpFunctionDao {

	// back
	public List<Map<String, Object>> list(EmpFunction empFunction);
	public void add(EmpFunction empFunction);
	public void update(EmpFunction empFunction);
	public void delete(EmpFunction empFunction);
	
}

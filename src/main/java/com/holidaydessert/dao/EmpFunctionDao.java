package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.EmpFunction;

public interface EmpFunctionDao {

	// back
	public List<Map<String, Object>> list(EmpFunction empFunction);
	public int getCount(EmpFunction empFunction);
	public void add(EmpFunction empFunction);
	public void update(EmpFunction empFunction);
	public List<Map<String, Object>> getIdToAuth(EmpFunction empFunction);
	
}

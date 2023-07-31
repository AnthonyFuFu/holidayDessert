package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.EmpFunction;

public interface EmpFunctionDao {

	public List<Map<String, Object>> list(EmpFunction empFunction);

}

package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.EmpFunctionDao;
import com.holidaydessert.model.EmpFunction;

@Service
public class EmpFunctionService {

	@Autowired
	private EmpFunctionDao empFunctionDao;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(EmpFunction empFunction) {
		return empFunctionDao.list(empFunction);
	}

	public int getCount(EmpFunction empFunction) {
		return empFunctionDao.getCount(empFunction);
	}

	public void add(EmpFunction empFunction) {
		empFunctionDao.add(empFunction);
	}

	public void update(EmpFunction empFunction) {
		empFunctionDao.update(empFunction);
	}

	public EmpFunction getData(EmpFunction empFunction) {
		return empFunctionDao.getData(empFunction);
	}
	
	public List<Map<String, Object>> getAdminListToAuth() {
		return empFunctionDao.getAdminListToAuth();
	}

}

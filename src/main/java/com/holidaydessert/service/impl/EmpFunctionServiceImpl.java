package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.EmpFunctionDao;
import com.holidaydessert.model.EmpFunction;
import com.holidaydessert.service.EmpFunctionService;

@Service
public class EmpFunctionServiceImpl implements EmpFunctionService {

	@Autowired
	private EmpFunctionDao empFunctionDao;

	@Override
	public List<Map<String, Object>> list(EmpFunction empFunction) {
		return empFunctionDao.list(empFunction);
	}

	@Override
	public int getCount(EmpFunction empFunction) {
		return empFunctionDao.getCount(empFunction);
	}

	@Override
	public void add(EmpFunction empFunction) {
		empFunctionDao.add(empFunction);
	}

	@Override
	public void update(EmpFunction empFunction) {
		empFunctionDao.update(empFunction);
	}

	@Override
	public EmpFunction getData(EmpFunction empFunction) {
		return empFunctionDao.getData(empFunction);
	}
	
	@Override
	public List<Map<String, Object>> getAdminListToAuth() {
		return empFunctionDao.getAdminListToAuth();
	}

}

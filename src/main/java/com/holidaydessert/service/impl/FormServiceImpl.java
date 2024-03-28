package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.FormDao;
import com.holidaydessert.model.Form;
import com.holidaydessert.service.FormService;

@Service
public class FormServiceImpl implements FormService {

	@Autowired
	private FormDao formdao;
	
	@Override
	public List<Map<String, Object>> list(Form form) {
		return formdao.list(form);
	}
	
	@Override
	public int getCount(Form form) {
		return formdao.getCount(form);
	}
	
	@Override
	public void add(Form form) {
		formdao.add(form);
	}
	
}

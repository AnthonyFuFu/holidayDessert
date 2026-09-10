package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.FormDao;
import com.holidaydessert.model.Form;
import com.holidaydessert.repository.FormRepository;

@Service
public class FormService {

	@Autowired
	private FormDao formdao;
	
	@Autowired
	private FormRepository formRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Form form) {
		return formdao.list(form);
	}
	
	public int getCount(Form form) {
		return formdao.getCount(form);
	}

	// =============================================
	// front
	// =============================================
	public void add(Form form) {
		formRepository.save(form);
	}
	
}

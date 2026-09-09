package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.FullcalendarDao;
import com.holidaydessert.model.Fullcalendar;

@Service
public class FullcalendarService {

	@Autowired
	private FullcalendarDao fullcalendarDao;
	
	// back
	public List<Map<String, Object>> list(Fullcalendar fullcalendar) {
		return fullcalendarDao.list(fullcalendar);
	}

	public List<Map<String, Object>> getManagedEmployees(Fullcalendar fullcalendar) {
		return fullcalendarDao.getManagedEmployees(fullcalendar);
	}
	
	public void add(Fullcalendar fullcalendar) {
		fullcalendarDao.add(fullcalendar);
	}

	public void update(Fullcalendar fullcalendar) {
		fullcalendarDao.update(fullcalendar);
	}
	
}

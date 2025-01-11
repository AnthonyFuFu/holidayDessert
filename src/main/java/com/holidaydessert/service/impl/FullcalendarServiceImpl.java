package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.FullcalendarDao;
import com.holidaydessert.model.Fullcalendar;
import com.holidaydessert.service.FullcalendarService;

@Service
public class FullcalendarServiceImpl implements FullcalendarService {
	
	@Autowired
	private FullcalendarDao fullcalendarDao;
	
	@Override
	public List<Map<String, Object>> list(Fullcalendar fullcalendar) {
		return fullcalendarDao.list(fullcalendar);
	}

	@Override
	public List<Map<String, Object>> getManagedEmployees(Fullcalendar fullcalendar) {
		return fullcalendarDao.getManagedEmployees(fullcalendar);
	}
	
	@Override
	public void add(Fullcalendar fullcalendar) {
		fullcalendarDao.add(fullcalendar);
	}

	@Override
	public void update(Fullcalendar fullcalendar) {
		fullcalendarDao.update(fullcalendar);
	}
	
}

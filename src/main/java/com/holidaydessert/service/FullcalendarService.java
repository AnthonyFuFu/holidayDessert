package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Fullcalendar;

public interface FullcalendarService {
	
	// back
	public List<Map<String, Object>> list(Fullcalendar fullcalendar);
	public List<Map<String, Object>> getManagedEmployees(Fullcalendar fullcalendar);
	public void add(Fullcalendar fullcalendar);
	public void update(Fullcalendar fullcalendar);
	
}

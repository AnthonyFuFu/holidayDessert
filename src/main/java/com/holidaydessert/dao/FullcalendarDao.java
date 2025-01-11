package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Fullcalendar;

public interface FullcalendarDao {
	
	// back
	public List<Map<String, Object>> list(Fullcalendar fullcalendar);
	public List<Map<String, Object>> getManagedEmployees(Fullcalendar fullcalendar);
	public void add(Fullcalendar fullcalendar);
	public void update(Fullcalendar fullcalendar);
	
}

package com.holidaydessert.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.FullcalendarDao;
import com.holidaydessert.model.Fullcalendar;

@Service
public class FullcalendarService {

	@Autowired
	private FullcalendarDao fullcalendarDao;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Fullcalendar fullcalendar) {
		return removeNullValues(fullcalendarDao.list(fullcalendar));
	}

	public List<Map<String, Object>> getManagedEmployees(Fullcalendar fullcalendar) {
		return removeNullValues(fullcalendarDao.getManagedEmployees(fullcalendar));
	}
	
	// 移除值為 null 的欄位：FullCalendar 會把 null 轉成字串 "null"
	// (例如 url 變成 href="null"、groupId 變成 "null" 讓所有事件被視為同一群組)
	private List<Map<String, Object>> removeNullValues(List<Map<String, Object>> list) {
		if (list != null) {
			for (Map<String, Object> map : list) {
				map.values().removeIf(Objects::isNull);
			}
		}
		return list;
	}

	public void add(Fullcalendar fullcalendar) {
		fullcalendarDao.add(fullcalendar);
	}

	// 是否為該假單員工的直屬主管
	public boolean isManagerOfEvent(String id, String managerEmpId) {
		String eventManagerId = fullcalendarDao.getEventManagerId(id);
		return eventManagerId != null && eventManagerId.equals(managerEmpId);
	}

	public void update(Fullcalendar fullcalendar) {
		fullcalendarDao.update(fullcalendar);
	}
	
}

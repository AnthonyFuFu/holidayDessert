package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.FullcalendarDao;
import com.holidaydessert.model.Fullcalendar;

@Repository
public class FullcalendarDaoImpl implements FullcalendarDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Map<String, Object>> list(Fullcalendar fullcalendar) {
		
	    List<Object> args = new ArrayList<>();
	    String sql = " SELECT id, title, " +
	                 " DATE_FORMAT(start, '%Y-%m-%dT%H:%i:%s') AS start, " +
	                 " DATE_FORMAT(end, '%Y-%m-%dT%H:%i:%s') AS end, " +
	                 " allDay, url, classNames, editable, startEditable, durationEditable, overlap, display, " +
	                 " backgroundColor, borderColor, textColor, groupId, extendedProps, isApproved, EMP_ID " +
	                 " FROM fullcalendar WHERE EMP_ID = ?";
	    // 添加參數
	    if (fullcalendar.getEmployee() != null && fullcalendar.getEmployee().getEmpId() != null) {
	        args.add(fullcalendar.getEmployee().getEmpId()); // 傳遞 EMP_ID
	    }
	    
	    // 查詢數據
	    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
	    
	    // 如果查詢有結果，返回結果；如果沒有，返回 null
	    if (list != null && list.size() > 0) {
	        return list;
	    } else {
	        return null;
	    }
	    
	}

	@Override
	public void add(Fullcalendar fullcalendar) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.fullcalendar "
				   + " (title, start, end, allDay, backgroundColor, borderColor,EMP_ID) "
				   + " VALUES(?, ?, ?, ?, ?, ?, ?) ";
		
		args.add(fullcalendar.getTitle());
		args.add(fullcalendar.getStart());
		args.add(fullcalendar.getEnd());
		args.add(fullcalendar.getAllDay());
		args.add(fullcalendar.getBackgroundColor());
		args.add(fullcalendar.getBorderColor());
		args.add(fullcalendar.getEmployee().getEmpId());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(Fullcalendar fullcalendar) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.fullcalendar "
				   + " SET backgroundColor = ?, borderColor = ?, isApproved = ? "
				   + " WHERE id = ? ";

		args.add(fullcalendar.getBackgroundColor());
		args.add(fullcalendar.getBorderColor());
		args.add(fullcalendar.getIsApproved());
		args.add(fullcalendar.getId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

}

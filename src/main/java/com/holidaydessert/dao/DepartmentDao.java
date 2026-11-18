package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Department;

@Repository
public class DepartmentDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
	public void add(Department department) {

		List<Object> args = new ArrayList<>();

		String sql = " INSERT INTO holiday_dessert.department "
				   + " (DEPT_NAME, DEPT_LOC) "
				   + " VALUES(?, ?) ";
		
		args.add(department.getDeptName());
		args.add(department.getDeptLoc());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	public void update(Department department) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.department "
				   + " SET DEPT_NAME = ?, DEPT_LOC = ? "
				   + " WHERE DEPT_ID = ? ";
		
		args.add(department.getDeptName());
		args.add(department.getDeptLoc());
		args.add(department.getDeptId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	public void delete(Department department) {

		List<Object> args = new ArrayList<>();
		
		String sql = " DELETE FROM holiday_dessert.department "
				   + " WHERE DEPT_ID = ? ";

		args.add(department.getDeptId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	public List<Map<String, Object>> getList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.department ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}
	
}

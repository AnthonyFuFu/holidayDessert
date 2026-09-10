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
	public List<Map<String, Object>> list(Department department) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.department ";
		
		if (department.getSearchText() != null && department.getSearchText().length() > 0) {
			String[] searchText = department.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(DEPT_NAME, ?) > 0"
					+  " OR INSTR(DEPT_LOC, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if (department.getStart() != null && !"".equals(department.getStart())) {
			sql += " LIMIT " + department.getStart() + "," + department.getLength();
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	public int getCount(Department department) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.department ";

		if (department.getSearchText() != null && department.getSearchText().length() > 0) {
			String[] searchText = department.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(DEPT_NAME, ?) > 0"
					+  " OR INSTR(DEPT_LOC, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

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

	public Department getData(Department department) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.department "
				   + " WHERE DEPT_ID = ? ";
		
		args.add(department.getDeptId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		Department dept = new Department();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        String deptId = String.valueOf(resultMap.get("DEPT_ID"));
	        String deptName = String.valueOf(resultMap.get("DEPT_NAME"));
	        String deptLoc = String.valueOf(resultMap.get("DEPT_LOC"));
	        
	        dept.setDeptId(deptId);
	        dept.setDeptName(deptName);
	        dept.setDeptLoc(deptLoc);
	        
	    }
		return dept == null ? null : dept;
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

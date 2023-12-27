package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.AuthorityDao;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;

@Repository
public class AuthorityDaoImpl implements AuthorityDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(Authority authority) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT a.EMP_ID, a.AUTH_STATUS, ef.* FROM holiday_dessert.authority a "
				   + " LEFT JOIN holiday_dessert.emp_function ef on ef.FUNC_ID = a.FUNC_ID "
				   + " WHERE EMP_ID = ? "
				   + " AND AUTH_STATUS = 1 ";
		
		args.add(authority.getEmpId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}
	
	@Override
	public List<Map<String, Object>> getAuthorityList(Authority authority) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT a.*,ef.FUNC_NAME FROM holiday_dessert.authority a "
				   + " LEFT JOIN holiday_dessert.emp_function ef on ef.FUNC_ID = a.FUNC_ID "
				   + " WHERE EMP_ID = ? ";
		
		args.add(authority.getEmpId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}
	
	@Override
	public int getCount(Authority authority) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.authority ";
		
		if(sql.indexOf("WHERE") > 0) {
			sql += " AND AUTH_STATUS = 1 ";
		} else {
			sql += " WHERE AUTH_STATUS = 1 ";
		}
		
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}
	
	@Override
	public void addAdminAuthority(Employee employee, List<Map<String, Object>> empFunction) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.authority "
				   + " (EMP_ID, FUNC_ID, AUTH_STATUS) ";
		
		if(empFunction.size() > 0) {
			for(int i=0; i<empFunction.size(); i++) {
				if(i == 0) {
					sql += " VALUES(?, ?, 1) ";
				} else {
					sql += " ,(?, ?, 1) ";
				}
				args.add(employee.getEmpId());
				args.add(empFunction.get(i).get("FUNC_ID"));
			}
		}
		
		jdbcTemplate.update(sql, args.toArray());
		
	}
	
	@Override
	public void addStaffAuthority(Employee employee, List<Map<String, Object>> empFunction) {
	    
	    List<Object> args = new ArrayList<>();
	    
	    String sql = " INSERT INTO holiday_dessert.authority "
	               + " (EMP_ID, FUNC_ID, AUTH_STATUS) VALUES ";
	    
	    if (empFunction.size() > 0) {
	        for (int i = 0; i < empFunction.size(); i++) {
	            if (i > 0) {
	                sql += ",";
	            }
	            if (empFunction.get(i).get("FUNC_ID").toString().equals("1") || empFunction.get(i).get("FUNC_ID").toString().equals("2")) {
	                sql += "(?, ?, 0) ";
	            } else {
	                sql += "(?, ?, 1) ";
	            }
	            args.add(employee.getEmpId());
	            args.add(empFunction.get(i).get("FUNC_ID"));
	        }
	    }
	    jdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void update(Authority authority) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.authority "
				   + " SET AUTH_STATUS = ? "
				   + " WHERE EMP_ID = ? "
				   + " AND FUNC_ID = ? ";
		
		args.add(authority.getAuthStatus());
		args.add(authority.getEmpId());
		args.add(authority.getFuncId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}
	
}
package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.EmployeeDao;
import com.holidaydessert.model.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.employee ";

		if (employee.getSearchText() != null && employee.getSearchText().length() > 0) {
			String[] searchText = employee.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(EMP_NAME, ?) > 0"
					+  " OR INSTR(EMP_PHONE, ?) > 0 "
					+  " OR INSTR(EMP_ACCOUNT, ?) > 0 "
					+  " OR INSTR(EMP_HIREDATE, ?) > 0 "
					+  " OR INSTR(EMP_EMAIL, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if(sql.indexOf("WHERE") > 0) {
			sql += " AND EMP_STATUS = 1 ";
		} else {
			sql += " WHERE EMP_STATUS = 1 ";
		}
		
		if (employee.getStart() != null && !"".equals(employee.getStart())) {
			sql += " LIMIT " + employee.getStart() + "," + employee.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public int getCount(Employee employee) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.employee ";
		
		if (employee.getSearchText() != null && employee.getSearchText().length() > 0) {
			String[] searchText = employee.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(EMP_NAME, ?) > 0"
					+  " OR INSTR(EMP_PHONE, ?) > 0 "
					+  " OR INSTR(EMP_ACCOUNT, ?) > 0 "
					+  " OR INSTR(EMP_HIREDATE, ?) > 0 "
					+  " OR INSTR(EMP_EMAIL, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if(sql.indexOf("WHERE") > 0) {
			sql += " AND EMP_STATUS = 1 ";
		} else {
			sql += " WHERE EMP_STATUS = 1 ";
		}
		
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.employee "
				   + " (EMP_NAME, EMP_PHONE, EMP_PICTURE, EMP_ACCOUNT, EMP_PASSWORD, EMP_EMAIL, EMP_LEVEL, EMP_STATUS, EMP_HIREDATE) "
				   + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW()) ";
		
		args.add(employee.getEmpName());
		args.add(employee.getEmpPhone());
		args.add(employee.getEmpPicture());
		args.add(employee.getEmpAccount());
		args.add(employee.getEmpPassword());
		args.add(employee.getEmpEmail());
		args.add(employee.getEmpLevel());
		args.add(employee.getEmpStatus());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.employee "
				   + " SET EMP_NAME = ? , EMP_PHONE = ?, EMP_PICTURE = ?, EMP_ACCOUNT = ?, EMP_PASSWORD = ?, EMP_EMAIL = ?, EMP_LEVEL = ? "
				   + " WHERE EMP_ID = ? ";
		
		args.add(employee.getEmpName());
		args.add(employee.getEmpPhone());
		args.add(employee.getEmpPicture());
		args.add(employee.getEmpAccount());
		args.add(employee.getEmpPassword());
		args.add(employee.getEmpEmail());
		args.add(employee.getEmpLevel());
		args.add(employee.getEmpId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void resign(Employee employee) {

		String sql = " UPDATE holiday_dessert.employee "
				   + " SET EMP_STATUS = 0 "
				   + " WHERE EMP_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { employee.getEmpId() });
		
	}

	@Override
	public Employee login(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.employee "
				   + " WHERE EMP_ACCOUNT = ? "
				   + " AND EMP_STATUS = '1' ";
		
		args.add(employee.getEmpAccount());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		Employee user = new Employee();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        System.out.println(resultMap);
	        String empId = String.valueOf(resultMap.get("EMP_ID"));
	        String empName = String.valueOf(resultMap.get("EMP_NAME"));
	        String empPhone = String.valueOf(resultMap.get("EMP_PHONE"));
	        String empPicture = String.valueOf(resultMap.get("EMP_PICTURE"));
	        String empAccount = String.valueOf(resultMap.get("EMP_ACCOUNT"));
	        String empPassword = String.valueOf(resultMap.get("EMP_PASSWORD"));
	        String empEmail = String.valueOf(resultMap.get("EMP_EMAIL"));
	        String empLevel = String.valueOf(resultMap.get("EMP_LEVEL")).equals("true") ? "1": "0";
	        String empStatus = String.valueOf(resultMap.get("EMP_STATUS")).equals("true") ? "1": "0";
	        String empHiredate = String.valueOf(resultMap.get("EMP_HIREDATE"));
	        
	        user.setEmpId(empId);
	        user.setEmpName(empName);
	        user.setEmpPhone(empPhone);
	        user.setEmpPicture(empPicture);
	        user.setEmpAccount(empAccount);
	        user.setEmpPassword(empPassword);
	        user.setEmpEmail(empEmail);
	        user.setEmpLevel(empLevel);
	        user.setEmpStatus(empStatus);
	        user.setEmpHiredate(empHiredate);
	    }
		return user == null ? null : user;
	}

}

package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Employee;

@Repository
public class EmployeeDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
	public String getNextId() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT EMP_ID "
				   + " FROM holiday_dessert.employee "
				   + " ORDER BY EMP_ID DESC "
				   + " LIMIT 1 ";

		return jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("EMP_ID").toString();
		
	}

	public void add(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.employee "
				   + " (EMP_NAME, DEPT_ID, EMP_PHONE, EMP_JOB, EMP_SALARY, EMP_PICTURE, EMP_IMAGE, EMP_ACCOUNT, EMP_PASSWORD, EMP_EMAIL, EMP_LEVEL, EMP_STATUS, EMP_HIREDATE) "
				   + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, NOW()) ";

		args.add(employee.getEmpName());
		args.add(employee.getDeptId());
		args.add(employee.getEmpPhone());
		args.add(employee.getEmpJob());
		args.add(employee.getEmpSalary());
		args.add(employee.getEmpPicture());
		args.add(employee.getEmpImage());
		args.add(employee.getEmpAccount());
		args.add(employee.getEmpPassword());
		args.add(employee.getEmpEmail());
		args.add(employee.getEmpLevel());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	public void update(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.employee "
				   + " SET EMP_NAME = ?, DEPT_ID = ?, EMP_PHONE = ?, EMP_JOB = ?, EMP_SALARY = ?, EMP_PICTURE = ?, EMP_IMAGE = ?, EMP_ACCOUNT = ?, EMP_PASSWORD = ?, EMP_EMAIL = ?, EMP_LEVEL = ? "
				   + " WHERE EMP_ID = ? ";
		
		args.add(employee.getEmpName());
		args.add(employee.getDeptId());
		args.add(employee.getEmpPhone());
		args.add(employee.getEmpJob());
		args.add(employee.getEmpSalary());
		args.add(employee.getEmpPicture());
		args.add(employee.getEmpImage());
		args.add(employee.getEmpAccount());
		args.add(employee.getEmpPassword());
		args.add(employee.getEmpEmail());
		args.add(employee.getEmpLevel());
		args.add(employee.getEmpId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	public void resign(Employee employee) {

		String sql = " UPDATE holiday_dessert.employee "
				   + " SET EMP_STATUS = 0 "
				   + " WHERE EMP_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { employee.getEmpId() });
		
	}

	public Employee getData(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.employee "
				   + " WHERE EMP_ID = ? ";
		
		args.add(employee.getEmpId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		Employee user = new Employee();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        String empId = String.valueOf(resultMap.get("EMP_ID"));
	        String empName = String.valueOf(resultMap.get("EMP_NAME"));
	        String deptId = String.valueOf(resultMap.get("DEPT_ID"));
	        String empPhone = String.valueOf(resultMap.get("EMP_PHONE"));
	        String empJob = String.valueOf(resultMap.get("EMP_JOB"));
	        String empSalary = String.valueOf(resultMap.get("EMP_SALARY"));
	        String empPicture = String.valueOf(resultMap.get("EMP_PICTURE"));
	        String empImage = String.valueOf(resultMap.get("EMP_IMAGE"));
	        String empAccount = String.valueOf(resultMap.get("EMP_ACCOUNT"));
	        String empPassword = String.valueOf(resultMap.get("EMP_PASSWORD"));
	        String empEmail = String.valueOf(resultMap.get("EMP_EMAIL"));
	        String empLevel = String.valueOf(resultMap.get("EMP_LEVEL"));
	        String empStatus = String.valueOf(resultMap.get("EMP_STATUS"));
	        String empHiredate = String.valueOf(resultMap.get("EMP_HIREDATE"));
	        String empTheme = String.valueOf(resultMap.get("EMP_THEME"));
	        
	        user.setEmpId(empId);
	        user.setDeptId(deptId);
	        user.setEmpName(empName);
	        user.setEmpPhone(empPhone);
	        user.setEmpJob(empJob);
	        user.setEmpSalary(empSalary);
	        user.setEmpPicture(empPicture);
	        user.setEmpImage(empImage);
	        user.setEmpAccount(empAccount);
	        user.setEmpPassword(empPassword);
	        user.setEmpEmail(empEmail);
	        user.setEmpLevel(empLevel);
	        user.setEmpStatus(empStatus);
	        user.setEmpHiredate(empHiredate);
	        user.setEmpTheme(empTheme);
	    }
		return user == null ? null : user;
	}

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
	        String empId = String.valueOf(resultMap.get("EMP_ID"));
	        String empName = String.valueOf(resultMap.get("EMP_NAME"));
	        String deptId = String.valueOf(resultMap.get("DEPT_ID"));
	        String empPhone = String.valueOf(resultMap.get("EMP_PHONE"));
	        String empJob = String.valueOf(resultMap.get("EMP_JOB"));
	        String empSalary = String.valueOf(resultMap.get("EMP_SALARY"));
	        String empPicture = String.valueOf(resultMap.get("EMP_PICTURE"));
	        String empImage = String.valueOf(resultMap.get("EMP_IMAGE"));
	        String empAccount = String.valueOf(resultMap.get("EMP_ACCOUNT"));
	        String empPassword = String.valueOf(resultMap.get("EMP_PASSWORD"));
	        String empEmail = String.valueOf(resultMap.get("EMP_EMAIL"));
	        String empLevel = String.valueOf(resultMap.get("EMP_LEVEL"));
	        String empStatus = String.valueOf(resultMap.get("EMP_STATUS"));
	        String empHiredate = String.valueOf(resultMap.get("EMP_HIREDATE"));
	        String empTheme = String.valueOf(resultMap.get("EMP_THEME"));
	        
	        user.setEmpId(empId);
	        user.setDeptId(deptId);
	        user.setEmpName(empName);
	        user.setEmpPhone(empPhone);
	        user.setEmpJob(empJob);
	        user.setEmpSalary(empSalary);
	        user.setEmpPicture(empPicture);
	        user.setEmpImage(empImage);
	        user.setEmpAccount(empAccount);
	        user.setEmpPassword(empPassword);
	        user.setEmpEmail(empEmail);
	        user.setEmpLevel(empLevel);
	        user.setEmpStatus(empStatus);
	        user.setEmpHiredate(empHiredate);
	        user.setEmpTheme(empTheme);
	    }
		return user == null ? null : user;
	}

	public void updateTheme(Employee employee) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.employee "
				   + " SET EMP_THEME = ? "
				   + " WHERE EMP_ID = ? ";
		
		args.add(employee.getEmpTheme());
		args.add(employee.getEmpId());
		
		jdbcTemplate.update(sql, args.toArray());
	}
	
}

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
		
		String sql = " SELECT emp.EMP_ID, emp.DEPT_ID, EMP_NAME, EMP_PHONE, EMP_JOB, EMP_SALARY, EMP_PICTURE, EMP_IMAGE, "
				   + " EMP_ACCOUNT, EMP_PASSWORD, EMP_EMAIL, EMP_LEVEL, EMP_STATUS, DATE_FORMAT(EMP_HIREDATE, '%Y-%m-%d') EMP_HIREDATE, "
				   + " dept.DEPT_NAME,dept.DEPT_LOC "
				   + " FROM holiday_dessert.employee emp "
				   + " LEFT JOIN department dept ON dept.DEPT_ID = emp.DEPT_ID ";
		
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
					+  " OR INSTR(EMP_JOB, ?) > 0 "
					+  " OR INSTR(EMP_SALARY, ?) > 0 "
					+  " OR INSTR(EMP_PHONE, ?) > 0 "
					+  " OR INSTR(EMP_ACCOUNT, ?) > 0 "
					+  " OR INSTR(EMP_HIREDATE, ?) > 0 "
					+  " OR INSTR(EMP_EMAIL, ?) > 0 "
					+  " OR INSTR(DEPT_NAME, ?) > 0 "
					+  " OR INSTR(DEPT_LOC, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
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
				   + " FROM holiday_dessert.employee emp "
				   + " LEFT JOIN department dept ON dept.DEPT_ID = emp.DEPT_ID ";
		
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
					+  " OR INSTR(EMP_JOB, ?) > 0 "
					+  " OR INSTR(EMP_SALARY, ?) > 0 "
					+  " OR INSTR(EMP_PHONE, ?) > 0 "
					+  " OR INSTR(EMP_ACCOUNT, ?) > 0 "
					+  " OR INSTR(EMP_HIREDATE, ?) > 0 "
					+  " OR INSTR(EMP_EMAIL, ?) > 0 "
					+  " OR INSTR(DEPT_NAME, ?) > 0 "
					+  " OR INSTR(DEPT_LOC, ?) > 0 "
					+  " ) ";
			  	args.add(searchText[i]);
			  	args.add(searchText[i]);
			  	args.add(searchText[i]);
			  	args.add(searchText[i]);
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

	@Override
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

	@Override
	public void resign(Employee employee) {

		String sql = " UPDATE holiday_dessert.employee "
				   + " SET EMP_STATUS = 0 "
				   + " WHERE EMP_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { employee.getEmpId() });
		
	}

	@Override
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
	    }
		return user == null ? null : user;
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
	    }
		return user == null ? null : user;
	}

}

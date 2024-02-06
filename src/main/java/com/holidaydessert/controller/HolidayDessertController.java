package com.holidaydessert.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Department;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/")
@SessionAttributes("memberSession")
@CrossOrigin
//@RestController
@Api(tags = "前台")
public class HolidayDessertController {
	
	@Autowired
	private EmployeeService employeeService;
	
//	private final org.slf4j.Logger log = LoggerFactory.getLogger(LoggerGroups.class);
	
	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "首頁", httpMethod = "GET", notes = "進行查詢")
	public String index(Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		List<Employee> list = employeeService.findAllWithDepartment();
		for (Employee employee : list) {
		    Department department = employee.getDepartment();
		    System.out.println(department.toString());
		    System.out.println("Employee: " + employee.getEmpName() + ", Department: " + department.getDeptName());
		}
		
		return "front/index";

	}
	
}

package com.holidaydessert.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Department;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.EmployeeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Controller
@RequestMapping("/")
@SessionAttributes("memberSession")
@Tag(name = "前台")
public class HolidayDessertController {
	
	@Autowired
	private EmployeeService employeeService;
	
//	private final org.slf4j.Logger log = LoggerFactory.getLogger(LoggerGroups.class);
	
	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	@Operation(summary = "首頁", description = "進行查詢")
	public String index(Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		List<Employee> list = employeeService.findAllWithDepartment();
		for (Employee employee : list) {
		    Department department = employee.getDepartment();
		    System.out.println(department.toString()+"|===|"+"Employee: " + employee.getEmpName() + ", Department: " + department.getDeptName());
		}
		
		return "front/index";

	}
	
}

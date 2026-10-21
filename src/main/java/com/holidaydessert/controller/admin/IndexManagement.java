package com.holidaydessert.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Employee;
import com.holidaydessert.service.EmployeeService;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/admin")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class IndexManagement {
	
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(@SessionAttribute Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		return "admin/index";
	}

	@RequestMapping(value = "/updateTheme" , method = {RequestMethod.GET, RequestMethod.POST})
	public void updateTheme(HttpServletRequest pRequest, HttpServletResponse pResponse, HttpSession session, Model model) {
		
		String empId = pRequest.getParameter("empId")!=null ? pRequest.getParameter("empId") : "";
		String empTheme = pRequest.getParameter("empTheme")!=null ? pRequest.getParameter("empTheme") : "";
		
		Employee employee = new Employee();
		employee.setEmpId(empId);
		employee.setEmpTheme(empTheme);
		
		employeeService.updateTheme(employee);
		
		Employee employeeSession = (Employee) session.getAttribute("employeeSession");
		employeeSession.setEmpTheme(empTheme);
		session.setAttribute("employeeSession",employeeSession);
		
	}
	
}

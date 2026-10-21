package com.holidaydessert.controller.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;
import java.util.function.Supplier;

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
import com.holidaydessert.service.DashboardService;
import com.holidaydessert.service.EmployeeService;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class IndexManagement {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private DashboardService dashboardService;
	
	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(@SessionAttribute Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		LocalDateTime now = LocalDateTime.now();
		int hour = now.getHour();
		model.addAttribute("greeting", hour < 11 ? "早安" : hour < 18 ? "午安" : "晚安");
		model.addAttribute("today", now.format(DateTimeFormatter.ofPattern("yyyy年M月d日 EEEE", Locale.TAIWAN)));
		model.addAttribute("updatedAt", now.format(DateTimeFormatter.ofPattern("HH:mm")));
		
		// 每個區塊各自查詢，單一區塊失敗不影響整個首頁
		model.addAttribute("revenue", safe("revenue", dashboardService::revenue, Collections.emptyMap()));
		model.addAttribute("orderStatus", safe("orderStatus", dashboardService::orderStatus, Collections.emptyList()));
		model.addAttribute("revenueTrend", safe("revenueTrend", dashboardService::revenueTrend, Collections.emptyList()));
		model.addAttribute("recentOrders", safe("recentOrders", dashboardService::recentOrders, Collections.emptyList()));
		model.addAttribute("topProducts", safe("topProducts", dashboardService::topProducts, Collections.emptyList()));
		model.addAttribute("members", safe("members", dashboardService::members, Collections.emptyMap()));
		model.addAttribute("content", safe("content", dashboardService::content, Collections.emptyMap()));
		model.addAttribute("schedules", safe("schedules", dashboardService::schedules, Collections.emptyList()));
		model.addAttribute("ticketSummary", safe("ticketSummary", dashboardService::ticketSummary, Collections.emptyMap()));
		model.addAttribute("ticketEvents", safe("ticketEvents", dashboardService::ticketEvents, Collections.emptyList()));
		model.addAttribute("system", safe("system", dashboardService::system, Collections.emptyMap()));
		model.addAttribute("recentActions", safe("recentActions", dashboardService::recentActions, Collections.emptyList()));
		
		return "admin/index";
	}
	
	private <T> T safe(String name, Supplier<T> supplier, T fallback) {
		try {
			return supplier.get();
		} catch (Exception e) {
			log.error("[Dashboard] {} 查詢失敗", name, e);
			return fallback;
		}
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

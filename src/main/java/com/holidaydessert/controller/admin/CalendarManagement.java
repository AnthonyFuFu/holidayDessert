package com.holidaydessert.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Fullcalendar;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.FullcalendarService;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/admin/calendar")
@SessionAttributes("employeeSession")
@CrossOrigin
@ApiIgnore
public class CalendarManagement {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private FullcalendarService fullcalendarService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, @ModelAttribute Employee employee, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);
		return "admin/calendar/list";

	}

	@ResponseBody
	@GetMapping("/listFullcalendar")
	public List<Map<String, Object>>listFullcalendar(@RequestParam(value = "EMP_ID", required = true) String empId) throws Exception {
		
		Fullcalendar fullcalendarData = new Fullcalendar();
		Employee employee = new Employee();
		employee.setEmpId(empId);
		fullcalendarData.setEmployee(employee);
		
		List<Map<String, Object>> fullcalendarList = fullcalendarService.list(fullcalendarData);
		
		if (fullcalendarList == null) {
			fullcalendarList = new ArrayList<Map<String, Object>>();
		}
	    return fullcalendarList;
	}

	@ResponseBody
	@GetMapping("/getManagedEmployees")
	public List<Map<String, Object>>getManagedEmployees(@RequestParam(value = "EMP_ID", required = true) String empId) throws Exception {
		System.out.println("empId:"+empId);
		Fullcalendar fullcalendarData = new Fullcalendar();
		Employee employee = new Employee();
		employee.setEmpManagerId(empId);
		fullcalendarData.setEmployee(employee);
		
		List<Map<String, Object>> getManagedEmpList = fullcalendarService.getManagedEmployees(fullcalendarData);

		System.out.println("getManagedEmpList:"+getManagedEmpList);
		if (getManagedEmpList == null) {
			getManagedEmpList = new ArrayList<Map<String, Object>>();
		}
	    return getManagedEmpList;
	}

	@RequestMapping(value = "/calendarAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String calendarAddSubmit(
			@RequestParam(value = "title", required = true) String title,
			@RequestParam(value = "start", required = true) String start,
			@RequestParam(value = "end", required = true) String end,
			@RequestParam(value = "empId", required = true) String empId,
			Model model) throws Exception {
		
		Fullcalendar fullcalendarData = new Fullcalendar();
		fullcalendarData.setTitle(title);
		fullcalendarData.setStart(start);
		fullcalendarData.setEnd(end);
		fullcalendarData.setAllDay("0");
		fullcalendarData.setBackgroundColor("#808080");
		fullcalendarData.setBorderColor("#808080");
		if (fullcalendarData.getEmployee() == null) {
		    fullcalendarData.setEmployee(new Employee());
		}
		fullcalendarData.getEmployee().setEmpId(empId);
		
		try {
			fullcalendarService.add(fullcalendarData);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/calendar/list");

		return "admin/toPath";
	}
	
	@ResponseBody
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public Map<String, String> approve(
	        @RequestParam(value = "id", required = true) String id) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        Fullcalendar fullcalendarData = new Fullcalendar();
	        fullcalendarData.setId(id);
	        fullcalendarData.setIsApproved("1");
	        fullcalendarData.setBackgroundColor("#32CD32");
	        fullcalendarData.setBorderColor("#32CD32");
	        fullcalendarService.update(fullcalendarData);

	        response.put("status", "success");
	        response.put("message", "准假成功");
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "error");
	        response.put("message", "審核失敗：" + e.getMessage());
	    }
	    return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/notApprove", method = RequestMethod.POST)
	public Map<String, String> notApprove(
	        @RequestParam(value = "id", required = true) String id) {
	    Map<String, String> response = new HashMap<>();
		try {
			Fullcalendar fullcalendarData = new Fullcalendar();
			fullcalendarData.setId(id);
			fullcalendarData.setIsApproved("0");
			fullcalendarData.setBackgroundColor("#f56954");
			fullcalendarData.setBorderColor("#f56954");
			fullcalendarService.update(fullcalendarData);
			
	        response.put("status", "success");
	        response.put("message", "更新為不准假");
		} catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "error");
	        response.put("message", "審核失敗：" + e.getMessage());
	    }
	    return response;
	}
	
}

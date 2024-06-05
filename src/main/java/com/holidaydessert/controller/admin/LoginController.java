package com.holidaydessert.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.holidaydessert.model.Employee;
import com.holidaydessert.service.EmployeeService;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/admin")
@ApiIgnore
public class LoginController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/login")
    public String login(@ModelAttribute Employee employee, Model model){
		return "admin/login";
    }
	
	@RequestMapping(value = "/doLogin" , method = {RequestMethod.POST})
	public String doLogin(@ModelAttribute Employee employee, HttpSession session,
		@RequestParam(value="empAccount",required = true) String empAccount,
		@RequestParam(value="empPassword",required = true) String empPassword, Model model,
		HttpServletRequest pRequest){
		String msg = "";
//		String ip = pRequest.getRemoteAddr();
		try {
			employee.setEmpAccount(empAccount);
			employee.setEmpPassword(empPassword);
			
			Employee login = employeeService.login(employee);
			//檢查帳號密碼是否符合
			if(login.getEmpId() != null) {
				
				if("0".equals(login.getEmpStatus())) {
					msg = "此帳號已被停權！";
					model.addAttribute("msg", msg);
					return "admin/login";
				} else {
					session.setAttribute("employeeSession", login);
					session.setMaxInactiveInterval(60* 60);
					model.addAttribute("PATH", "index");
					return "admin/toPath";
				}
			//檢查最高管理者
			} else if("admin".equals(employee.getEmpAccount()) && "123456".equals(employee.getEmpPassword())) {
				employee.setEmpAccount("admin");
				employee.setEmpName("最高管理者");
				employee.setEmpLevel("0");
				session.setAttribute("employeeSession", employee);
				session.setMaxInactiveInterval(60* 600);
				model.addAttribute("PATH", "index");
				return "admin/toPath";
			} else {
				msg = "帳號或密碼輸入錯誤！";
				model.addAttribute("msg", msg);
				return "admin/login";
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return "admin/login";
    }
	
	@RequestMapping(value = "/logout", method = {RequestMethod.GET})
	public String logout(@ModelAttribute Employee employee, HttpSession session, Model model){
		
		if(session.getAttribute("employeeSession") != null){
			session.removeAttribute("employeeSession");
		}
		
		return "admin/login";
	}
	
}

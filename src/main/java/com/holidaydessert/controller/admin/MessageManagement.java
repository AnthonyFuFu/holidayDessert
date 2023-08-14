package com.holidaydessert.controller.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.AuthorityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/message")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "訊息管理")
public class MessageManagement {

	@Value("${web.path}")
	private String WEB_PATH;
	
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "訊息清單", httpMethod = "GET", notes = "進行訊息查詢")
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);
		return "admin/message/list";

	}

}

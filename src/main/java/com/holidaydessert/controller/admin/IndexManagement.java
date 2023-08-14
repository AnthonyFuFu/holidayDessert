package com.holidaydessert.controller.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin")
@SessionAttributes("employeeSession")
@CrossOrigin
//@RestController
@Api(tags = "後台")
public class IndexManagement {
	
	@Autowired
	private AuthorityService authorityService;

	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "後台首頁", httpMethod = "GET", notes = "進行查詢首頁")
	public String index(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);

		return "admin/index";

	}
}

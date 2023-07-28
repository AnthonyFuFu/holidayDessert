package com.holidayDessert.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.holidayDessert.model.Authority;
import com.holidayDessert.service.AuthorityService;

@Controller
@RequestMapping("/companyInformation")
public class CompanyInformationController {
	
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping(value = "/index" , method = {RequestMethod.GET, RequestMethod.POST})
	public String companyInformation(HttpSession session, Model model, HttpServletRequest pRequest) {
		
		Authority authority = new Authority();
		
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		System.out.println(authorityList);
		
		return "front/holidayDessert/index";
	}
	
}

package com.holidaydessert.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/")
@SessionAttributes("memberSession")
@CrossOrigin
//@RestController
@Api(tags = "前台")
public class HolidayDessertController {
	
//	private final org.slf4j.Logger log = LoggerFactory.getLogger(LoggerGroups.class);
	
	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "首頁", httpMethod = "GET", notes = "進行查詢")
	public String index(Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		return "front/index";

	}
	
}

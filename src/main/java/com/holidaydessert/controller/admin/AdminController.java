package com.holidaydessert.controller.admin;

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
@RequestMapping("/admin")
@SessionAttributes("employeeSession")
@CrossOrigin
//@RestController
@Api(tags = "後台")
public class AdminController {

	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "後台首頁", httpMethod = "GET", notes = "進行查詢首頁")
	public String index(Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {

		return "admin/index";

	}
}

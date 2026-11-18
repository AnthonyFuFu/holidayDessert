package com.holidaydessert.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Employee;
import com.holidaydessert.service.MainOrderService;
import com.holidaydessert.service.OrderDetailService;
import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/admin/order")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class OrderManagement {
	
	@Autowired
	private MainOrderService mainOrderService;
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model) throws Exception {
		
		return "admin/order/list";
	}
	
	@ResponseBody
	@GetMapping("/orderTables")
	public Map<String, Object> orderTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return mainOrderService.list(params);
	}

	@ResponseBody
	@GetMapping("/orderDetailTables")
	public Map<String, Object> orderDetailTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return orderDetailService.list(params);
	}
	
}

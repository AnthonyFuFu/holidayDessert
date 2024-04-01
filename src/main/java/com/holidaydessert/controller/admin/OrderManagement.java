package com.holidaydessert.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.MainOrder;
import com.holidaydessert.model.OrderDetail;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.MainOrderService;
import com.holidaydessert.service.OrderDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/order")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "訂單管理")
public class OrderManagement {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MainOrderService mainOrderService;
	
	@Autowired
	private OrderDetailService orderDetailService;

	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "訂單清單", httpMethod = "GET", notes = "進行訂單查詢")
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);
		return "admin/order/list";

	}
	
	@GetMapping("/orderTables")
	public void orderTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute MainOrder mainOrder, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		MainOrder mainOrderData = new MainOrder();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		mainOrderData.setStart(start);
		mainOrderData.setLength(length);
		mainOrderData.setSearchText(searchValue);
		
		List<Map<String, Object>> orderList = mainOrderService.list(mainOrderData);

		if (orderList == null) {
			orderList = new ArrayList<Map<String, Object>>();
		}

		int count = mainOrderService.getCount(mainOrderData);

		mainOrder.setRecordsFiltered(count);
		mainOrder.setRecordsTotal(count);
		mainOrder.setData(orderList);
		mainOrder.setDraw(Integer.valueOf(draw));
		
		String output = gson.toJson(mainOrder);

		pResponse.setCharacterEncoding("utf-8");
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/orderDetailTables")
	public void orderDetailTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute OrderDetail orderDetail, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		OrderDetail orderDetailData = new OrderDetail();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		orderDetailData.setStart(start);
		orderDetailData.setLength(length);
		orderDetailData.setSearchText(searchValue);
		
		List<Map<String, Object>> orderDetailList = orderDetailService.list(orderDetailData);

		if (orderDetailList == null) {
			orderDetailList = new ArrayList<Map<String, Object>>();
		}

		int count = orderDetailService.getCount(orderDetailData);

		orderDetail.setRecordsFiltered(count);
		orderDetail.setRecordsTotal(count);
		orderDetail.setData(orderDetailList);
		orderDetail.setDraw(Integer.valueOf(draw));
		
		String output = gson.toJson(orderDetail);

		pResponse.setCharacterEncoding("utf-8");
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}

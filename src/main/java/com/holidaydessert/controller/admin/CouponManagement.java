package com.holidaydessert.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.CouponService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/coupon")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "優惠券管理")
public class CouponManagement {

	@Value("${web.path}")
	private String WEB_PATH;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private CouponService couponService;

	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "優惠券清單", httpMethod = "GET", notes = "進行優惠券查詢")
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);
		return "admin/coupon/list";

	}
	
	@GetMapping("/couponTables")
	public void couponTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Coupon coupon, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Coupon couponData = new Coupon();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		couponData.setStart(start);
		couponData.setLength(length);
		couponData.setSearchText(searchValue);
		
		List<Map<String, Object>> couponList = couponService.list(couponData);

		if (couponList == null) {
			couponList = new ArrayList<Map<String, Object>>();
		}

		int count = couponService.getCount(couponData);

		coupon.setRecordsFiltered(count);
		coupon.setRecordsTotal(count);
		coupon.setData(couponList);
		coupon.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(coupon);

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
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
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.PromotionDetailService;
import com.holidaydessert.service.PromotionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/promotion")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "優惠活動管理")
public class PromotionManagement {

	@Value("${web.path}")
	private String WEB_PATH;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private PromotionDetailService promotionDetailService;

	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "優惠活動清單", httpMethod = "GET", notes = "進行優惠活動查詢")
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);
		return "admin/promotion/list";

	}
	
	@GetMapping("/promotionTables")
	public void promotionTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Promotion promotion, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Promotion promotionData = new Promotion();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		promotionData.setStart(start);
		promotionData.setLength(length);
		promotionData.setSearchText(searchValue);
		
		List<Map<String, Object>> promotionList = promotionService.list(promotionData);

		if (promotionList == null) {
			promotionList = new ArrayList<Map<String, Object>>();
		}

		int count = promotionService.getCount(promotionData);

		promotion.setRecordsFiltered(count);
		promotion.setRecordsTotal(count);
		promotion.setData(promotionList);
		promotion.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(promotion);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@GetMapping("/promotionDetailTables")
	public void promotionDetailTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		PromotionDetail promotionDetailData = new PromotionDetail();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		promotionDetailData.setStart(start);
		promotionDetailData.setLength(length);
		promotionDetailData.setSearchText(searchValue);
		
		List<Map<String, Object>> promotionDetailList = promotionDetailService.list(promotionDetailData);

		if (promotionDetailList == null) {
			promotionDetailList = new ArrayList<Map<String, Object>>();
		}

		int count = promotionDetailService.getCount(promotionDetailData);

		promotionDetail.setRecordsFiltered(count);
		promotionDetail.setRecordsTotal(count);
		promotionDetail.setData(promotionDetailList);
		promotionDetail.setDraw(Integer.valueOf(draw));
		
		String output = gson.toJson(promotionDetail);

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

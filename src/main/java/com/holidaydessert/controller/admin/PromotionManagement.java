package com.holidaydessert.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
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
import com.holidaydessert.model.Product;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.ProductService;
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
	private ProductService productService;
	
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
	@RequestMapping(value = "/issuePromotionTables", method = { RequestMethod.GET, RequestMethod.POST })
	public void issuePromotionTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Product product, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");
		
		Product productData = new Product();
		productData.setStart(start);
		productData.setLength(length);
		productData.setSearchText(searchValue);
		
		List<Map<String, Object>> productList = productService.issuePromotionList(productData);
		if (productList == null) {
			productList = new ArrayList<Map<String, Object>>();
		}
		
		int count = productService.getIssuePromotionCount(productData);
		
		product.setRecordsFiltered(count);
		product.setRecordsTotal(count);
		product.setData(productList);
		product.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(product);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@RequestMapping(value = "/addPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addPromotion(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			Promotion promotion = new Promotion();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("promotion", promotion);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/promotion/promotionForm";
	}
	
	@RequestMapping(value = "/updatePromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePromotion(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Promotion promotion, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			promotion = promotionService.getData(promotion);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("promotion", promotion);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/promotion/promotionForm";
	}
	
	@RequestMapping(value = "/promotionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String promotionAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Promotion promotion, HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionService.add(promotion);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/promotion/list");
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/promotionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String promotionUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Promotion promotion, HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionService.update(promotion);
			model.addAttribute("PATH", "/holidayDessert/admin/promotion/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/issuePromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issuePromotion(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("promotionList", promotionList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/promotion/promotionDetailForm";
	}

	@RequestMapping(value = "/issueDailyPromotionSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueDailyPromotionSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest,Model model) throws Exception {

		String[] products = pRequest.getParameter("products")!=null ? pRequest.getParameter("products").split(",") : null;
		String promotionId = pRequest.getParameter("promotionId")!=null ? pRequest.getParameter("promotionId") : null;
		
		Promotion promotion = new Promotion();
		promotion.setPmId(promotionId);
		promotion = promotionService.getData(promotion);
		
		try {
			promotionDetailService.batchAddOneDayPromotion(promotion, products);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/promotion/list");

		return "admin/toPath";
	}

	@RequestMapping(value = "/issueWeeklyPromotionSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueWeeklyPromotionSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		String[] products = pRequest.getParameter("products")!=null ? pRequest.getParameter("products").split(",") : null;
		String promotionId = pRequest.getParameter("promotionId")!=null ? pRequest.getParameter("promotionId") : null;
		
		Promotion promotion = new Promotion();
		promotion.setPmId(promotionId);
		promotion = promotionService.getData(promotion);
		
		try {
			promotionDetailService.batchAddOneWeekPromotion(promotion, products);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/promotion/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/addOneProductPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addOneProductPromotion(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> productList = productService.issueOneProductList();
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			PromotionDetail promotionDetail = new PromotionDetail();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productList", productList);
			model.addAttribute("promotionList", promotionList);
			model.addAttribute("promotionDetail", promotionDetail);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/promotion/oneProductPromotionForm";
	}
	
	@RequestMapping(value = "/updateOneProductPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateOneProductPromotion(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> productList = productService.getList();
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			promotionDetail = promotionDetailService.getData(promotionDetail);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productList", productList);
			model.addAttribute("promotionList", promotionList);
			model.addAttribute("promotionDetail", promotionDetail);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/promotion/oneProductPromotionForm";
	}

	@RequestMapping(value = "/oneProductPromotionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String oneProductPromotionAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionDetailService.addOne(promotionDetail);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/promotion/list");

		return "admin/toPath";
	}

	@RequestMapping(value = "/oneProductPromotionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String oneProductPromotionUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionDetailService.update(promotionDetail);
			model.addAttribute("PATH", "/holidayDessert/admin/promotion/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
}

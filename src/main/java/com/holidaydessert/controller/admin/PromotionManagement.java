package com.holidaydessert.controller.admin;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.service.ProductService;
import com.holidaydessert.service.PromotionDetailService;
import com.holidaydessert.service.PromotionService;

import io.swagger.v3.oas.annotations.Hidden;
import static com.holidaydessert.constant.BuildPath.*;

@Controller
@RequestMapping("/admin/promotion")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class PromotionManagement {
	
	@Autowired
	private PromotionService promotionService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private PromotionDetailService promotionDetailService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model) throws Exception {
		
		return "admin/promotion/list";
	}
	
	@ResponseBody
	@GetMapping("/promotionTables")
	public Map<String, Object> promotionTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return promotionService.list(params);
	}
	
	@ResponseBody
	@GetMapping("/promotionDetailTables")
	public Map<String, Object> promotionDetailTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return promotionDetailService.list(params);
	}
	
	@ResponseBody
	@RequestMapping(value = "/issuePromotionTables", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> issuePromotionTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return productService.issuePromotionList(params);
	}
	
	@RequestMapping(value = "/addPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addPromotion(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		try {
			Promotion promotion = new Promotion();
			model.addAttribute("promotion", promotion);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/promotion/promotionForm";
	}
	
	@RequestMapping(value = "/updatePromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePromotion(@SessionAttribute Employee employeeSession,
			@ModelAttribute Promotion promotion, Model model) throws Exception {
		
		try {
			promotion = promotionService.getData(promotion);
			model.addAttribute("promotion", promotion);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/promotion/promotionForm";
	}
	
	@RequestMapping(value = "/promotionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String promotionAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Promotion promotion, HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionService.add(promotion);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", ADMIN_PROMOTION_LIST);
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/promotionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String promotionUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Promotion promotion, HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionService.update(promotion);
			model.addAttribute("PATH", ADMIN_PROMOTION_LIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/issuePromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issuePromotion(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			model.addAttribute("promotionList", promotionList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/promotion/promotionDetailForm";
	}

	@RequestMapping(value = "/issueDailyPromotionSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueDailyPromotionSubmit(@SessionAttribute Employee employeeSession,
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
		model.addAttribute("PATH", ADMIN_PROMOTION_LIST);

		return "admin/toPath";
	}

	@RequestMapping(value = "/issueWeeklyPromotionSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueWeeklyPromotionSubmit(@SessionAttribute Employee employeeSession,
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
		model.addAttribute("PATH", ADMIN_PROMOTION_LIST);

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/addOneProductPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addOneProductPromotion(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		List<Map<String, Object>> productList = productService.issueOneProductList();
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			PromotionDetail promotionDetail = new PromotionDetail();
			model.addAttribute("productList", productList);
			model.addAttribute("promotionList", promotionList);
			model.addAttribute("promotionDetail", promotionDetail);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/promotion/oneProductPromotionForm";
	}
	
	@RequestMapping(value = "/updateOneProductPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateOneProductPromotion(@SessionAttribute Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, Model model) throws Exception {
		
		List<Map<String, Object>> productList = productService.getList();
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			promotionDetail = promotionDetailService.getData(promotionDetail);
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
	public String oneProductPromotionAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionDetailService.addOne(promotionDetail);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", ADMIN_PROMOTION_LIST);

		return "admin/toPath";
	}

	@RequestMapping(value = "/oneProductPromotionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String oneProductPromotionUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			promotionDetailService.update(promotionDetail);
			model.addAttribute("PATH", ADMIN_PROMOTION_LIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
}

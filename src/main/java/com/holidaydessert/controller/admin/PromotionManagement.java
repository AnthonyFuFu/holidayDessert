package com.holidaydessert.controller.admin;

import java.util.Collections;
import java.util.Map;

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
import static com.holidaydessert.utils.ControllerUtil.*;

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
	public String addPromotion(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("promotion", new Promotion());
		return "admin/promotion/promotionForm";
	}
	
	@RequestMapping(value = "/updatePromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePromotion(@SessionAttribute Employee employeeSession,
			@ModelAttribute Promotion promotion, Model model) {

		model.addAttribute("promotion", promotionService.getData(promotion));
		return "admin/promotion/promotionForm";
	}
	
	@RequestMapping(value = "/promotionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String promotionAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Promotion promotion, Model model) {

		return addSubmit(model, ADMIN_PROMOTION_LIST, () -> promotionService.add(promotion));
	}
	
	@RequestMapping(value = "/promotionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String promotionUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Promotion promotion, Model model) {

		return updateSubmit(model, ADMIN_PROMOTION_LIST, () -> promotionService.update(promotion));
	}
	
	@RequestMapping(value = "/issuePromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issuePromotion(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("promotionList", safe("promotionList", promotionService::getList, Collections.emptyList()));
		return "admin/promotion/promotionDetailForm";
	}

	@RequestMapping(value = "/issueDailyPromotionSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueDailyPromotionSubmit(@SessionAttribute Employee employeeSession,
			@RequestParam(required = false) String products,
			@RequestParam(required = false) String promotionId, Model model) {

		return addSubmit(model, ADMIN_PROMOTION_LIST, () -> {
			Promotion promotion = new Promotion();
			promotion.setPmId(promotionId);
			promotionDetailService.batchAddOneDayPromotion(promotionService.getData(promotion), products != null ? products.split(",") : null);
		});
	}

	@RequestMapping(value = "/issueWeeklyPromotionSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueWeeklyPromotionSubmit(@SessionAttribute Employee employeeSession,
			@RequestParam(required = false) String products,
			@RequestParam(required = false) String promotionId, Model model) {

		return addSubmit(model, ADMIN_PROMOTION_LIST, () -> {
			Promotion promotion = new Promotion();
			promotion.setPmId(promotionId);
			promotionDetailService.batchAddOneWeekPromotion(promotionService.getData(promotion), products != null ? products.split(",") : null);
		});
	}
	
	@RequestMapping(value = "/addOneProductPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addOneProductPromotion(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("productList", safe("productList", productService::issueOneProductList, Collections.emptyList()));
		model.addAttribute("promotionList", safe("promotionList", promotionService::getList, Collections.emptyList()));
		model.addAttribute("promotionDetail", new PromotionDetail());
		return "admin/promotion/oneProductPromotionForm";
	}
	
	@RequestMapping(value = "/updateOneProductPromotion" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateOneProductPromotion(@SessionAttribute Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, Model model) {

		model.addAttribute("productList", safe("productList", productService::getList, Collections.emptyList()));
		model.addAttribute("promotionList", safe("promotionList", promotionService::getList, Collections.emptyList()));
		model.addAttribute("promotionDetail", promotionDetailService.getData(promotionDetail));
		return "admin/promotion/oneProductPromotionForm";
	}

	@RequestMapping(value = "/oneProductPromotionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String oneProductPromotionAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, Model model) {

		return addSubmit(model, ADMIN_PROMOTION_LIST, () -> promotionDetailService.addOne(promotionDetail));
	}

	@RequestMapping(value = "/oneProductPromotionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String oneProductPromotionUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute PromotionDetail promotionDetail, Model model) {

		return updateSubmit(model, ADMIN_PROMOTION_LIST, () -> promotionDetailService.update(promotionDetail));
	}
	
}

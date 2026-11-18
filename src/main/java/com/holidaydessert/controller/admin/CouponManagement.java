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
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.CouponService;
import com.holidaydessert.service.MemberCouponService;
import com.holidaydessert.service.MemberService;
import io.swagger.v3.oas.annotations.Hidden;
import static com.holidaydessert.constant.BuildPath.*;

@Controller
@RequestMapping("/admin/coupon")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class CouponManagement {

	@Autowired
	private CouponService couponService;

	@Autowired
	private MemberCouponService memberCouponService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model) throws Exception {
		
		return "admin/coupon/list";
	}
	
	@ResponseBody
	@GetMapping("/couponTables")
	public Map<String, Object> couponTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return couponService.list(params);
	}

	@ResponseBody
	@GetMapping("/memberCouponTables")
	public Map<String, Object> memberCouponTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return memberCouponService.list(params);
	}
	
	@ResponseBody
	@GetMapping("/issueCouponTables")
	public Map<String, Object> issueCouponTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return memberService.issueCouponList(params);
	}
	
	@RequestMapping(value = "/addCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addCoupon(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		try {
			Coupon coupon = new Coupon();
			model.addAttribute("coupon", coupon);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/coupon/couponForm";
	}
	
	@RequestMapping(value = "/updateCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateCoupon(@SessionAttribute Employee employeeSession,
			@ModelAttribute Coupon coupon, Model model) throws Exception {
		
		try {
			coupon = couponService.getData(coupon);
			model.addAttribute("coupon", coupon);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/coupon/couponForm";
	}
	
	@RequestMapping(value = "/couponAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String couponAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Coupon coupon,
			@RequestParam MultipartFile imageFile,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
	        String uploadPath = buildUploadPath(COUPON_IMAGE_FOLDER);
	        coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
	        coupon.setCpPicture(COUPON_WEB_PATH + coupon.getCpImage());
			couponService.add(coupon);
			
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", ADMIN_COUPON_LIST);

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/couponUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String couponUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Coupon coupon,
			@RequestParam MultipartFile imageFile,
			@RequestParam(required = false) String originalImage,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			// 若原image_url 不為空且無新檔案名稱，則image_url 設為原始image_url
			if (originalImage != null && imageFile.getOriginalFilename().equals("")) {
				coupon.setCpImage(originalImage);
			} else {
	            String uploadPath = buildUploadPath(COUPON_IMAGE_FOLDER);
	            commonService.deleteUploadedFiles(originalImage, uploadPath);
	            coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
			}
	        coupon.setCpPicture(COUPON_WEB_PATH + coupon.getCpImage());
			couponService.update(coupon);
			model.addAttribute("PATH", ADMIN_COUPON_LIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}

	@RequestMapping(value = "/issueCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueCoupon(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		List<Map<String, Object>> couponList = couponService.getList();
		try {
			model.addAttribute("couponList", couponList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/coupon/memberCouponForm";
	}

	@RequestMapping(value = "/issueDailyCouponSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueDailyCouponSubmit(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest,Model model) throws Exception {

		String[] members = pRequest.getParameter("members")!=null ? pRequest.getParameter("members").split(",") : null;
		String couponId = pRequest.getParameter("couponId")!=null ? pRequest.getParameter("couponId") : null;
		
		Coupon coupon = new Coupon();
		coupon.setCpId(couponId);
		
		try {
			memberCouponService.batchAddOneDayCoupon(coupon, members);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", ADMIN_COUPON_LIST);

		return "admin/toPath";
	}

	@RequestMapping(value = "/issueWeeklyCouponSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueWeeklyCouponSubmit(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		String[] members = pRequest.getParameter("members")!=null ? pRequest.getParameter("members").split(",") : null;
		String couponId = pRequest.getParameter("couponId")!=null ? pRequest.getParameter("couponId") : null;
		
		Coupon coupon = new Coupon();
		coupon.setCpId(couponId);
		
		try {
			memberCouponService.batchAddOneWeekCoupon(coupon, members);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", ADMIN_COUPON_LIST);

		return "admin/toPath";
	}

}

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Coupon;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.MemberCoupon;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.CouponService;
import com.holidaydessert.service.MemberCouponService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/coupon")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "優惠券管理")
public class CouponManagement {

	@Value("${admin.upload.file.path}")
	private String ADMIN_UPLOAD_FILE_PATH;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private CouponService couponService;

	@Autowired
	private MemberCouponService memberCouponService;

	@Autowired
	private CommonService commonService;

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

	@GetMapping("/memberCouponTables")
	public void memberCouponTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute MemberCoupon memberCoupon, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		MemberCoupon memberCouponData = new MemberCoupon();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		memberCouponData.setStart(start);
		memberCouponData.setLength(length);
		memberCouponData.setSearchText(searchValue);
		
		List<Map<String, Object>> memberCouponList = memberCouponService.list(memberCouponData);

		if (memberCouponList == null) {
			memberCouponList = new ArrayList<Map<String, Object>>();
		}

		int count = memberCouponService.getCount(memberCouponData);

		memberCoupon.setRecordsFiltered(count);
		memberCoupon.setRecordsTotal(count);
		memberCoupon.setData(memberCouponList);
		memberCoupon.setDraw(Integer.valueOf(draw));
		
		String output = gson.toJson(memberCoupon);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@RequestMapping(value = "/addCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addCoupon(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			Coupon coupon = new Coupon();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("coupon", coupon);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/coupon/couponForm";
	}
	
	@RequestMapping(value = "/updateCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateCoupon(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Coupon coupon, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			coupon = couponService.getData(coupon);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("coupon", coupon);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/coupon/couponForm";
	}
	
	@RequestMapping(value = "/couponAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String couponAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Coupon coupon,
			@RequestParam(value = "imageFile") MultipartFile imageFile,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			String osName = System.getProperty("os.name").toLowerCase();

			if (osName.contains("win")) {
				coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images\\coupon\\"));
			} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
				coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/coupon/"));
			} else if (osName.contains("mac")) {
				coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/coupon/"));
			} else {
				coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/coupon/"));
			}
			coupon.setCpPicture("holidayDessert/admin/upload/images/coupon/" + coupon.getCpImage());
			couponService.add(coupon);
			
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/coupon/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/couponUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String couponUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Coupon coupon,
			@RequestParam(value = "imageFile") MultipartFile imageFile,
			@RequestParam(value = "originalImage", required = false) String originalImage,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			// 若原image_url 不為空且無新檔案名稱，則image_url 設為原始image_url
			if (originalImage != null && imageFile.getOriginalFilename().equals("")) {
				coupon.setCpImage(originalImage);
			} else {
				String osName = System.getProperty("os.name").toLowerCase();
				if (osName.contains("win")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images\\coupon\\");
					coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images\\coupon\\"));
				} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/coupon/");
					coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/coupon/"));
				} else if (osName.contains("mac")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/coupon/");
					coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/coupon/"));
				} else {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/coupon/");
					coupon.setCpImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/coupon/"));
				}
			}
			coupon.setCpPicture("holidayDessert/admin/upload/images/coupon/" + coupon.getCpImage());
			
			couponService.update(coupon);
			model.addAttribute("PATH", "/holidayDessert/admin/coupon/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}

	@RequestMapping(value = "/issueCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueCoupon(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			Coupon coupon = new Coupon();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("coupon", coupon);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/coupon/memberCouponForm";
	}

	@RequestMapping(value = "/issueDailyCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueDailyCoupon(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			Coupon coupon = new Coupon();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("coupon", coupon);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/coupon/couponForm";
	}

	@RequestMapping(value = "/issueWeeklyCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueWeeklyCoupon(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			Coupon coupon = new Coupon();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("coupon", coupon);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/coupon/couponForm";
	}

	@RequestMapping(value = "/issueMonthlyCoupon" , method = {RequestMethod.GET, RequestMethod.POST})
	public String issueMonthlyCoupon(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			Coupon coupon = new Coupon();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("coupon", coupon);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/coupon/couponForm";
	}
	
}

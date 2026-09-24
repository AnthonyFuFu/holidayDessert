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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Banner;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.News;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.BannerService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.NewsService;
import com.holidaydessert.service.PromotionService;
import static com.holidaydessert.constant.BuildPath.*;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/admin/news")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class NewsManagement {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private NewsService newsService;

	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private BannerService bannerService;

	@Autowired
	private CommonService commonService;

	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		List<Map<String, Object>> newsList = newsService.getListForBanner();
		
		for (int i = 0; i < newsList.size(); i++) {
			Banner banner = new Banner();
			banner.setNewsId(newsList.get(i).get("NEWS_ID").toString());
			List<Map<String, Object>> bannerList = bannerService.list(banner);
			newsList.get(i).put("bannerList", bannerList);
		}
		
		model.addAttribute("authorityList", authorityList);
		model.addAttribute("newsList", newsList);
		return "admin/news/list";

	}
	
	@ResponseBody
	@GetMapping("/newsTables")
	public Map<String, Object> newsTables(@RequestParam Map<String, String> params) {
		return newsService.list(params);
	}

	@RequestMapping(value = "/addNews" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addNews(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			News news = new News();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("promotionList", promotionList);
			model.addAttribute("news", news);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/news/newsForm";
	}
	
	@RequestMapping(value = "/updateNews" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateNews(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> promotionList = promotionService.getList();
		
		try {
			news = newsService.getData(news);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("promotionList", promotionList);
			model.addAttribute("news", news);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/news/newsForm";
	}
	
	@RequestMapping(value = "/newsAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String newsAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			newsService.add(news);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/news/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/newsUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String newsUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			newsService.update(news);
			model.addAttribute("PATH", "/holidayDessert/admin/news/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}

	@RequestMapping(value = "/editBanner" , method = {RequestMethod.GET, RequestMethod.POST})
	public String editBanner(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news,@ModelAttribute Banner banner, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		banner.setNewsId(news.getNewsId());
		List<Map<String, Object>> bannerList = bannerService.list(banner);
		
		try {
			news = newsService.getData(news);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("news", news);
			model.addAttribute("bannerList", bannerList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/news/editBanner";
	}

	@RequestMapping(value = "/addBanner" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addBanner(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		news = newsService.getData(news);
		
		List<Map<String, Object>> newsList = newsService.getList();
		
		try {
			Banner banner = new Banner();
			banner.setNewsId(news.getNewsId());
			banner.setNewsName(news.getNewsName());
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("newsList", newsList);
			model.addAttribute("banner", banner);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/news/bannerForm";
	}
	
	@RequestMapping(value = "/updateBanner" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateBanner(@SessionAttribute Employee employeeSession,
			@ModelAttribute Banner banner, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			banner = bannerService.getData(banner);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("banner", banner);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/news/bannerForm";
	}
	

	@RequestMapping(value = "/bannerAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String bannerAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Banner banner,
			@RequestParam MultipartFile imageFile,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
	        String uploadPath = buildUploadPath(BANNER_IMAGE_FOLDER);
	        banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
	        banner.setBanPicture(BANNER_WEB_PATH + banner.getBanImage());
			bannerService.add(banner);
			
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/news/editBanner?newsId="+banner.getNewsId());

		return "admin/toPath";
	}

	@RequestMapping(value = "/bannerUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String bannerUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Banner banner,
			@RequestParam MultipartFile imageFile,
			@RequestParam(required = false) String originalImage,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			// 若原image_url 不為空且無新檔案名稱，則image_url 設為原始image_url
			if (originalImage != null && imageFile.getOriginalFilename().equals("")) {
				banner.setBanImage(originalImage);
			} else {
	            String uploadPath = buildUploadPath(BANNER_IMAGE_FOLDER);
	            commonService.deleteUploadedFiles(originalImage, uploadPath);
	            banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
			}
	        banner.setBanPicture(BANNER_WEB_PATH + banner.getBanImage());
			bannerService.update(banner);
			model.addAttribute("PATH", "/holidayDessert/admin/news/editBanner?newsId="+banner.getNewsId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}

	@ResponseBody
	@PostMapping(value = "/bannerDelete")
	public String bannerDelete(@SessionAttribute Employee employeeSession,
			@ModelAttribute Banner banner, Model model, HttpServletRequest request) {
		
		banner = bannerService.getData(banner);
		bannerService.delete(banner);
		
		return "editBanner?newsId="+banner.getNewsId();
	}
	
}

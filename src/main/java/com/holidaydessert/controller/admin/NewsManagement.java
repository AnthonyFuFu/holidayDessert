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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Banner;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.News;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.BannerService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.NewsService;
import com.holidaydessert.service.PromotionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/news")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "最新消息管理")
public class NewsManagement {

	@Value("${admin.upload.file.path}")
	private String ADMIN_UPLOAD_FILE_PATH;
	
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

	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "最新消息清單", httpMethod = "GET", notes = "進行最新消息查詢")
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
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
	
	@GetMapping("/newsTables")
	public void newsTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute News news, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		News newsData = new News();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		newsData.setStart(start);
		newsData.setLength(length);
		newsData.setSearchText(searchValue);
		
		List<Map<String, Object>> newsList = newsService.list(newsData);

		if (newsList == null) {
			newsList = new ArrayList<Map<String, Object>>();
		}

		int count = newsService.getCount(newsData);

		news.setRecordsFiltered(count);
		news.setRecordsTotal(count);
		news.setData(newsList);
		news.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(news);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/addNews" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addNews(@SessionAttribute("employeeSession") Employee employeeSession,
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
	public String updateNews(@SessionAttribute("employeeSession") Employee employeeSession,
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
	public String newsAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
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
	public String newsUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
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
	public String editBanner(@SessionAttribute("employeeSession") Employee employeeSession,
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
	public String addBanner(@SessionAttribute("employeeSession") Employee employeeSession,
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
	public String updateBanner(@SessionAttribute("employeeSession") Employee employeeSession,
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
	public String bannerAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Banner banner,
			@RequestParam(value = "imageFile") MultipartFile imageFile,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			String osName = System.getProperty("os.name").toLowerCase();

			if (osName.contains("win")) {
				banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images\\banner\\"));
			} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
				banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/banner/"));
			} else if (osName.contains("mac")) {
				banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/banner/"));
			} else {
				banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/banner/"));
			}
			banner.setBanPicture("holidayDessert/admin/upload/images/banner/" + banner.getBanImage());
			
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
	public String bannerUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Banner banner,
			@RequestParam(value = "imageFile") MultipartFile imageFile,
			@RequestParam(value = "originalImage", required = false) String originalImage,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			// 若原image_url 不為空且無新檔案名稱，則image_url 設為原始image_url
			if (originalImage != null && imageFile.getOriginalFilename().equals("")) {
				banner.setBanImage(originalImage);
			} else {
				String osName = System.getProperty("os.name").toLowerCase();
				if (osName.contains("win")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images\\banner\\");
					banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images\\banner\\"));
				} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/banner/");
					banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/banner/"));
				} else if (osName.contains("mac")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/banner/");
					banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/banner/"));
				} else {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/banner/");
					banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/banner/"));
				}
			}
			banner.setBanPicture("holidayDessert/admin/upload/images/banner/" + banner.getBanImage());
			
			bannerService.update(banner);
			model.addAttribute("PATH", "/holidayDessert/admin/news/editBanner?newsId="+banner.getNewsId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/bannerDelete" , method = {RequestMethod.POST})
	@ResponseBody
	public String bannerDelete(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Banner banner, Model model, HttpServletRequest request) {
		
		banner = bannerService.getData(banner);
		bannerService.delete(banner);
		
		return "editBanner?newsId="+banner.getNewsId();
	}
	
}

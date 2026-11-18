package com.holidaydessert.controller.admin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

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

import com.holidaydessert.model.Banner;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.News;
import com.holidaydessert.service.BannerService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.NewsService;
import com.holidaydessert.service.PromotionService;
import static com.holidaydessert.constant.BuildPath.*;
import static com.holidaydessert.utils.ControllerUtil.*;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/admin/news")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class NewsManagement {
	
	@Autowired
	private NewsService newsService;

	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private BannerService bannerService;

	@Autowired
	private CommonService commonService;

	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model) throws Exception {
		
		List<Map<String, Object>> newsList = newsService.getListForBanner();
		
		for (int i = 0; i < newsList.size(); i++) {
			Banner banner = new Banner();
			banner.setNewsId(newsList.get(i).get("NEWS_ID").toString());
			List<Map<String, Object>> bannerList = bannerService.list(banner);
			newsList.get(i).put("bannerList", bannerList);
		}
		model.addAttribute("newsList", newsList);
		return "admin/news/list";

	}
	
	@ResponseBody
	@GetMapping("/newsTables")
	public Map<String, Object> newsTables(@RequestParam Map<String, String> params) {
		return newsService.list(params);
	}

	@RequestMapping(value = "/addNews" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addNews(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("promotionList", safe("promotionList", promotionService::getList, Collections.emptyList()));
		model.addAttribute("news", new News());
		return "admin/news/newsForm";
	}
	
	@RequestMapping(value = "/updateNews" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateNews(@SessionAttribute Employee employeeSession, @ModelAttribute News news, Model model) {

		model.addAttribute("promotionList", safe("promotionList", promotionService::getList, Collections.emptyList()));
		model.addAttribute("news", newsService.getData(news));
		return "admin/news/newsForm";
	}
	
	@RequestMapping(value = "/newsAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String newsAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news, Model model) {

		return addSubmit(model, ADMIN_NEWS_LIST, () -> newsService.add(news));
	}
	
	@RequestMapping(value = "/newsUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String newsUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news, Model model) {

		return updateSubmit(model, ADMIN_NEWS_LIST, () -> newsService.update(news));
	}

	@RequestMapping(value = "/editBanner" , method = {RequestMethod.GET, RequestMethod.POST})
	public String editBanner(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news, @ModelAttribute Banner banner, Model model) {

		banner.setNewsId(news.getNewsId());
		model.addAttribute("news", newsService.getData(news));
		model.addAttribute("bannerList", bannerService.list(banner));
		return "admin/news/editBanner";
	}

	@RequestMapping(value = "/addBanner" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addBanner(@SessionAttribute Employee employeeSession,
			@ModelAttribute News news, Model model) {

		News newsData = newsService.getData(news);
		Banner banner = new Banner();
		banner.setNewsId(newsData.getNewsId());
		banner.setNewsName(newsData.getNewsName());
		model.addAttribute("newsList", safe("newsList", newsService::getList, Collections.emptyList()));
		model.addAttribute("banner", banner);
		return "admin/news/bannerForm";
	}
	
	@RequestMapping(value = "/updateBanner" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateBanner(@SessionAttribute Employee employeeSession,
			@ModelAttribute Banner banner, Model model) {

		model.addAttribute("banner", bannerService.getData(banner));
		return "admin/news/bannerForm";
	}
	

	@RequestMapping(value = "/bannerAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String bannerAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Banner banner,
			@RequestParam MultipartFile imageFile, Model model) {

		return addSubmit(model, adminBannerPath(banner.getNewsId()), () -> {
			String uploadPath = buildUploadPath(BANNER_IMAGE_FOLDER);
			banner.setBanImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
			banner.setBanPicture(BANNER_WEB_PATH + banner.getBanImage());
			bannerService.add(banner);
		});
	}

	@RequestMapping(value = "/bannerUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String bannerUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Banner banner,
			@RequestParam MultipartFile imageFile,
			@RequestParam(required = false) String originalImage, Model model) {

		return updateSubmit(model, adminBannerPath(banner.getNewsId()), () -> {
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
		});
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

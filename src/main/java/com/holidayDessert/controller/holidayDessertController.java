package com.holidayDessert.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidayDessert.model.Authority;
import com.holidayDessert.model.Banner;
import com.holidayDessert.service.AuthorityService;
import com.holidayDessert.service.BannerService;

@Controller
@SessionAttributes("memberSession")
@RequestMapping("/")
public class holidayDessertController {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private BannerService bannerService;
	
	@RequestMapping(value = "/index" , method = {RequestMethod.GET, RequestMethod.POST})
	public String index(Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {

		Authority authority = new Authority();
		
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		System.out.println(authorityList);
		
		Banner banner = new Banner();
		
		List<Map<String, Object>> bannerList = bannerService.list(banner);
		System.out.println(bannerList);
		
		return "front/holidayDessert/index";
	}
	
}

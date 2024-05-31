package com.holidaydessert.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.BannerService;
import com.holidaydessert.service.NewsService;
import com.holidaydessert.service.ProductPicService;
import com.holidaydessert.service.ProductService;
import com.holidaydessert.utils.JWTUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class IndexController {

	@Autowired
	private NewsService newsService;

	@Autowired
	private BannerService bannerService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductPicService productPicService;

	private static String subject = "holidaydessertAPI";
	
	@PostMapping("/getNewList")
	@ApiOperation(value = "代碼列表" , notes = "依類型及備註取得代碼列表")
//	@RequestMapping(value = "/getNewList", method = RequestMethod.POST)
	public ResponseEntity<?> sendForm(
//			@ApiParam(name="type",value="類型",required=false) 
//	@RequestParam(value ="type",required=false)String type,
//			@ApiParam(name = "memo" ,value = "備註",required = false)
//	@RequestParam(value = "memo" , required = false)String memo,
//			@ApiParam(name="sorts",value="排序方式",required=true)
//	@RequestParam(value ="sorts",required=true)String[] sorts,
	HttpServletRequest request) {
		
		ApiReturnObject apiReturnObject = new ApiReturnObject();
		String token = request.getHeader("Authorization");
		
		try {
			if(JWTUtil.getSubject(token) != null && JWTUtil.getSubject(token).equals(subject)) {
				List<Map<String, Object>> frontNewList = new ArrayList<>();
				frontNewList = productService.frontNewList();
				apiReturnObject.setResult(frontNewList);
			}else {
				throw new AuthException();
			}
		} catch (AuthException e) {
			Map<String, Object> map = new HashMap<>();
			map.put("status", "403");
			map.put("message", "驗證失敗");
			return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
}

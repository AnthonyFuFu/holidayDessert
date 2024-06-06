package com.holidaydessert.controller;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.ProductService;
import com.holidaydessert.utils.JWTUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = { "首頁" })
public class IndexController {

	@Autowired
	private ProductService productService;
	
//	@Autowired
//	private NewsService newsService;

//	@Autowired
//	private BannerService bannerService;
	
	private static String subject = "holidaydessertAPI";

    @ApiIgnore
	@RequestMapping(value =  {"/", "/index"}, method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "首頁", httpMethod = "POST", notes = "導回首頁")
    public RedirectView index(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/holidayDessert/index.html"); // 設置要跳轉的URL
		return redirectView;
    }

	@RequestMapping(value = "/getPopularList", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "熱門推薦", httpMethod = "POST" , notes = "顯示於首頁的熱門推薦清單")
	public ResponseEntity<?> getPopularList() {
		
		ApiReturnObject apiReturnObject = productService.getNewArrivalList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/getNewArrivalList", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "新品上市", httpMethod = "POST" , notes = "顯示於首頁的新品上市清單")
	public ResponseEntity<?> getNewArrivalList() {
		
		ApiReturnObject apiReturnObject = productService.getNewArrivalList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/getNewList", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "新品上市", httpMethod = "POST" , notes = "顯示於首頁的新品上市清單")
	public ResponseEntity<?> getNewList(HttpServletRequest request) {
		
		ApiReturnObject apiReturnObject = new ApiReturnObject();
		String token = request.getHeader("Authorization");
		
		try {
			if(JWTUtil.getSubject(token) != null && JWTUtil.getSubject(token).equals(subject)) {
				apiReturnObject = productService.getNewArrivalList();
			}else {
				throw new AuthException();
			}
		} catch (AuthException e) {
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("STATUS", "403");
			responseMap.put("MSG", "驗證失敗");
			return new ResponseEntity<>(responseMap, HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
}

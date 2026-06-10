package com.holidaydessert.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.CommentService;
import com.holidaydessert.service.ProductService;
import com.holidaydessert.utils.JWTUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Tag(name = "首頁")
public class IndexController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CommentService commentService;

//	@Autowired
//	private BannerService bannerService;
	
//	private static String subject = "holidaydessertAPI";

    @Hidden
    @PostMapping(value =  {"/", "/index"})
	@Operation(summary = "首頁", description = "導回首頁")
    public RedirectView index(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/holidayDessert/index.html"); // 設置要跳轉的URL
		return redirectView;
    }
    
    @PostMapping(value = "/getMainProductList")
	@Operation(summary = "主要產品", description = "顯示於首頁的主要產品清單")
	public ResponseEntity<?> getMainProductList() {
		
		ApiReturnObject apiReturnObject = productService.getMainProductList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}

    @PostMapping(value = "/getPopularList")
	@Operation(summary = "熱門推薦", description = "顯示於首頁的熱門推薦清單")
	public ResponseEntity<?> getPopularList() {
		
		ApiReturnObject apiReturnObject = productService.getNewArrivalList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
    @PostMapping(value = "/getNewArrivalList")
	@Operation(summary = "新品上市", description = "顯示於首頁的新品上市清單")
	public ResponseEntity<?> getNewArrivalList() {
		
		ApiReturnObject apiReturnObject = productService.getNewArrivalList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}

    @PostMapping(value = "/getCommentList")
	@Operation(summary = "留言", description = "顯示於首頁的留言清單")
	public ResponseEntity<?> getCommentList() {
		
		ApiReturnObject apiReturnObject = commentService.getCommentList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
    @PostMapping(value = "/getNewList")
	@Operation(summary = "新品上市", description = "顯示於首頁的新品上市清單")
	public ResponseEntity<?> getNewList(HttpServletRequest request) {
		try {
			ApiReturnObject apiReturnObject = new ApiReturnObject();
			String token = request.getHeader("Authorization");
			if(JWTUtil.getSubjectFromToken(token) != null) {
				apiReturnObject = productService.getNewArrivalList();
			}
			return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		} catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiReturnObject.forbidden("Token 無效"));
		}
	}
	
}

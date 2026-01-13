package com.holidaydessert.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = { "首頁" })
public class IndexController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CommentService commentService;

//	@Autowired
//	private BannerService bannerService;
	
//	private static String subject = "holidaydessertAPI";

    @ApiIgnore
    @PostMapping(value =  {"/", "/index"})
	@ApiOperation(value = "首頁", notes = "導回首頁")
    public RedirectView index(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/holidayDessert/index.html"); // 設置要跳轉的URL
		return redirectView;
    }
    
    @PostMapping(value = "/getMainProductList")
	@ApiOperation(value = "主要產品", notes = "顯示於首頁的主要產品清單")
	public ResponseEntity<?> getMainProductList() {
		
		ApiReturnObject apiReturnObject = productService.getMainProductList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}

    @PostMapping(value = "/getPopularList")
	@ApiOperation(value = "熱門推薦", notes = "顯示於首頁的熱門推薦清單")
	public ResponseEntity<?> getPopularList() {
		
		ApiReturnObject apiReturnObject = productService.getNewArrivalList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
    @PostMapping(value = "/getNewArrivalList")
	@ApiOperation(value = "新品上市", notes = "顯示於首頁的新品上市清單")
	public ResponseEntity<?> getNewArrivalList() {
		
		ApiReturnObject apiReturnObject = productService.getNewArrivalList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}

    @PostMapping(value = "/getCommentList")
	@ApiOperation(value = "留言", notes = "顯示於首頁的留言清單")
	public ResponseEntity<?> getCommentList() {
		
		ApiReturnObject apiReturnObject = commentService.getCommentList();
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
    @PostMapping(value = "/getNewList")
	@ApiOperation(value = "新品上市", httpMethod = "POST" , notes = "顯示於首頁的新品上市清單")
	public ResponseEntity<?> getNewList(HttpServletRequest request) {
		try {
			ApiReturnObject apiReturnObject = new ApiReturnObject();
			String token = request.getHeader("Authorization");
			if(JWTUtil.getSubjectFromToken(token) != null) {
				apiReturnObject = productService.getNewArrivalList();
			}
			return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		} catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiReturnObject(403, "Token 無效", null));
		}
	}
	
}

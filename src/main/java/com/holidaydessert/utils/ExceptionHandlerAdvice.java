package com.holidaydessert.utils;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerAdvice {
	@ExceptionHandler(value = Exception.class)
	public ModelAndView handleError(Exception ex, WebRequest request){
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.addObject("exception","此頁面不存在");
		if(ex.getMessage().indexOf("Missing session attribute") >= 0) {
			//後台導頁
			if(ex.getMessage().indexOf("employeeSession") >= 0) {
				modelAndView.setViewName("admin/login");
			}
			//前台導頁
			if(ex.getMessage().indexOf("memberSession") >= 0) {
				modelAndView.setViewName("front/jump");
			}
		} else {
//			modelAndView.setViewName("admin/error/404");
		}
		return modelAndView;
	}
	//如果我們要讓所有的@RequestMapping擁有此鍵值
//	@ModelAttribute
//	public void addAttribute(Model md){
//		md.addAttribute("message","你可以設定一些錯誤訊息");
//	}
}

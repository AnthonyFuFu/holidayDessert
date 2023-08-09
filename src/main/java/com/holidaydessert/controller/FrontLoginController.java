package com.holidaydessert.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.holidaydessert.model.Member;
import com.holidaydessert.service.MemberService;

@Controller
@RequestMapping("/front")
public class FrontLoginController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/login")
    public String login(@ModelAttribute Member member, Model model){
		return "front/login";
    }
	
	@RequestMapping(value = "/doLogin" , method = {RequestMethod.POST})
	public void doLogin(HttpServletRequest pRequest, HttpServletResponse pResponse, HttpSession session, Model model) {
		
		String memEmail = pRequest.getParameter("memEmail") != null ? pRequest.getParameter("memEmail") : "";
		String memPassword = pRequest.getParameter("memPassword") != null ? pRequest.getParameter("memPassword") : "";
		System.out.println(memEmail);
		System.out.println(memPassword);
		Map<String, Object> map = new HashMap<>();
		
		try {
			
			Member member = new Member();
			member.setMemEmail(memEmail);
			member.setMemPassword(memPassword);
			
			Member login = memberService.login(member);
			
			//檢查帳號密碼是否符合
			if(login != null && login.getMemEmail() != null) {
				
				member = login;
				
				if("0".equals(member.getMemStatus())) {
					map.put("STATUS", "N");
					map.put("MSG", "帳號未啟用");
				} else if ("0".equals(member.getMemVerificationStatus())) {
					map.put("STATUS", "N");
					map.put("MSG", "信箱認證未通過");
				} else {
					//session登錄
					session.setAttribute("memberSession", member);
					session.setMaxInactiveInterval(60*60);
					
					map.put("STATUS", "Y");
					map.put("MSG", "登入成功");
				}
				
			} else {
				map.put("STATUS", "N");
				map.put("MSG", "帳號或密碼輸入錯誤！");
			}
			
			JSONObject tJSONObject = new JSONObject(map);
			
			pResponse.setCharacterEncoding("utf-8");
			PrintWriter out = pResponse.getWriter();
			out.write(tJSONObject.toString());
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/logout" , method = {RequestMethod.POST})
	public void logout(HttpServletRequest pRequest, HttpServletResponse pResponse, HttpSession session, Model model) {
		
		if(session.getAttribute("memberSession") != null){
			session.removeAttribute("memberSession");
		}
		
	}
	
}

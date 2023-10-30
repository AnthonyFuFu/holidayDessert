package com.holidaydessert.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.base.Optional;
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

	@GetMapping("/google/login")
	public RedirectView googleLogin(@ModelAttribute Member member, HttpServletRequest pRequest, HttpServletResponse pResponse, Authentication authentication) {

		// 獲取google已驗證用戶的Principal
		Object principal = authentication.getPrincipal();
		String ip = getRemoteHost(pRequest);
		// 獲取HttpSession對象
		HttpSession session = pRequest.getSession();

		// 獲取google用戶的屬性
//		if (principal instanceof OAuth2User) {
//			OAuth2User oAuth2User = (OAuth2User) principal;
//			String oAuth2UserSub = (String) oAuth2User.getAttribute("sub");
//			Optional<MemberAccount> optional = memberAccountService.getDataByGoogleUid(oAuth2UserSub);
//			MemberAccount data = optional.orElse(null);
//			// 落點專案已有帳號
//			if (data != null) {
//				try {
//					Identity identity = new Identity();
//					identity.setName("前台");
//					Authorization authorization = new Authorization();
//					authorization.setMember_id(data.getId());
//					// 降級懲罰
//					int countAuth = authorizationService.getMemberAuth(authorization);
//					if (countAuth == 0 && "1".equals(data.getLevel())) {
//						identity.setLevel("2");
//						int identity_id = identityService.getFrontIdentity(identity);
//						data.setIdentity_id(String.valueOf(identity_id));
//						memberAccountService.updateIdentity(data);
//					}
//
//					MemberDetail memberDetail2 = new MemberDetail();
//					memberDetail2.setMember_id(data.getId());
//					int detailCount = memberDetailService.getCountByMember(memberDetail2);
//
//					List<Map<String, Object>> checkPreset = memberAccountService.getMembetPreset(data);
//					if (checkPreset != null) {
//						memberAccountService.updateExamNull(data);
//					}
//
//					if (detailCount == 0 || checkPreset != null) {
//						// 更新帳號權限
//						Identity identity2 = new Identity();
//						identity2.setName("前台");
//						identity2.setLevel("3");
//						int identity_id = identityService.getFrontIdentity(identity2);
//						MemberAccount memberAccount2 = new MemberAccount();
//						memberAccount2.setIdentity_id(String.valueOf(identity_id));
//						memberAccount2.setId(data.getId());
//						memberAccountService.updateIdentity(memberAccount2);
//
//						data.setLevel("3");
//						data.setIdentity_id(String.valueOf(identity_id));
//
//					}
//
//					if (!"".equals(data.getLevel()) && data.getLevel() != null
//							&& Integer.valueOf(data.getLevel()) <= 2) {
//						addMemberDetailCookie(data.getExam_number_id(), pRequest, pResponse);
//					}
//
//					// session登錄
//					session.setAttribute("memberAccountSession", data);
//					session.setMaxInactiveInterval(60 * 60);
//
//					LoginLog loginLog = new LoginLog();
//					loginLog.setGoogle_uid(oAuth2UserSub);
//					loginLog.setStatus("Y");
//					loginLog.setMsg("登入成功");
//					loginLog.setCategory("前台");
//					loginLog.setIp(ip);
//					loginLogService.add(loginLog);
//
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/forecast/index"); // 設置要跳轉的URL
		return redirectView;
	}
	
	
	private String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}
	
	
	
	
	
	
}

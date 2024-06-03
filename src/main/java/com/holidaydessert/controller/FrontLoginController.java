package com.holidaydessert.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.holidaydessert.model.Member;
import com.holidaydessert.service.MemberService;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/front")
public class FrontLoginController {
	
	@Autowired
	private MemberService memberService;
	
    @PostMapping("/login")
    public ResponseEntity<?> login(
    			@ApiParam(name = "Member", value = "會員", required = true) @RequestBody Member member,
    		HttpSession session) {
    	Map<String, Object> responseMap = new HashMap<>();
        try {
            Member login = memberService.login(member);
            // 檢查帳號密碼是否符合
            if (login != null && login.getMemEmail() != null) {
                if ("0".equals(login.getMemStatus())) {
                    responseMap.put("STATUS", "N");
                    responseMap.put("MSG", "帳號未啟用");
                } else if ("0".equals(login.getMemVerificationStatus())) {
                    responseMap.put("STATUS", "N");
                    responseMap.put("MSG", "信箱認證未通過");
                } else {
                    responseMap.put("STATUS", "Y");
                    responseMap.put("MSG", "登入成功");
                    responseMap.put("memberSession", login);
                }
            } else {
                responseMap.put("STATUS", "N");
                responseMap.put("MSG", "帳號或密碼輸入錯誤！");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            responseMap.put("STATUS", "N");
            responseMap.put("MSG", "伺服器錯誤");
        }
        return ResponseEntity.ok(responseMap);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
    	Map<String, Object> responseMap = new HashMap<>();
        try {
        	responseMap.put("STATUS", "Y");
        	responseMap.put("MSG", "登出成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            responseMap.put("STATUS", "N");
            responseMap.put("MSG", "伺服器錯誤");
        }
        return ResponseEntity.ok(responseMap);
    }
    
    @PostMapping("/getGoogleLogin")
    public ResponseEntity<?> getGoogleLogin(HttpServletRequest pRequest, Authentication authentication) {
    	Map<String, Object> responseMap = new HashMap<>();
    	
    	if (authentication != null) {
    		// 獲取google已驗證用戶的Principal
    		Object principal = authentication.getPrincipal();
    		// 獲取google用戶的屬性
    		if (principal instanceof OAuth2User) {
    			OAuth2User oAuth2User = (OAuth2User) principal;
    			String oAuth2UserSub = (String) oAuth2User.getAttribute("sub");
    			String oAuth2UserName = (String) oAuth2User.getAttribute("name");
    			String oAuth2UserEmail = (String) oAuth2User.getAttribute("email");
    			
    			Optional<Member> optional = memberService.getDataByGoogleUid(oAuth2UserSub);
    			Member memberAccountData = optional.orElse(null);
    			
    			System.out.println(oAuth2UserName+"-"+oAuth2UserEmail+"-"+oAuth2UserSub);
    			// 專案已有帳號
    			if (memberAccountData != null) {
                    responseMap.put("STATUS", "GLY");
                    responseMap.put("MSG", "登入成功");
    				responseMap.put("memberSession", memberAccountData);
    			} else {
                    responseMap.put("STATUS", "N");
                    responseMap.put("MSG", "登入失敗");
    			}
    		}
		} else {
            responseMap.put("STATUS", "GLN");
		}
        return ResponseEntity.ok(responseMap);
    }
    
	@GetMapping("/google/login")
	public RedirectView googleLogin(@ModelAttribute Member member, HttpServletRequest pRequest, HttpServletResponse pResponse, Authentication authentication) {

		// 獲取google已驗證用戶的Principal
		Object principal = authentication.getPrincipal();
		String ip = getRemoteHost(pRequest);
		// 獲取HttpSession對象
		HttpSession session = pRequest.getSession();

		// 獲取google用戶的屬性
		if (principal instanceof OAuth2User) {
			OAuth2User oAuth2User = (OAuth2User) principal;
			String oAuth2UserSub = (String) oAuth2User.getAttribute("sub");
			Optional<Member> optional = memberService.getDataByGoogleUid(oAuth2UserSub);
			Member data = optional.orElse(null);
			// 專案已有帳號
			if (data != null) {
				try {
					System.out.println(ip+"-"+session);
					
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
//					MemberDetail memberDetail2 = new MemberDetail();
//					memberDetail2.setMember_id(data.getId());
//					int detailCount = memberDetailService.getCountByMember(memberDetail2);
//					List<Map<String, Object>> checkPreset = memberAccountService.getMembetPreset(data);
//					if (checkPreset != null) {
//						memberAccountService.updateExamNull(data);
//					}
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
//						data.setLevel("3");
//						data.setIdentity_id(String.valueOf(identity_id));
//					}
//					if (!"".equals(data.getLevel()) && data.getLevel() != null
//							&& Integer.valueOf(data.getLevel()) <= 2) {
//						addMemberDetailCookie(data.getExam_number_id(), pRequest, pResponse);
//					}

					// session登錄
					session.setAttribute("memberSession", data);
					session.setMaxInactiveInterval(60 * 60);

//					LoginLog loginLog = new LoginLog();
//					loginLog.setGoogle_uid(oAuth2UserSub);
//					loginLog.setStatus("Y");
//					loginLog.setMsg("登入成功");
//					loginLog.setCategory("前台");
//					loginLog.setIp(ip);
//					loginLogService.add(loginLog);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/holidayDessert/index"); // 設置要跳轉的URL
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

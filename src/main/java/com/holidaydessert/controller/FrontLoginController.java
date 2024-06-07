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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.Member;
import com.holidaydessert.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/front")
@Api(tags = { "登入登出" })
public class FrontLoginController {
	
	@Autowired
	private MemberService memberService;
	
	@PostMapping(value = "/login")
	@ApiOperation(value = "登入", notes = "登入獲取會員資料")
    public ResponseEntity<?> login(@ApiParam(name = "Member", value = "會員", required = true) @RequestBody Member member) {
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

    @ApiIgnore
    @PostMapping(value = "/logout")
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

    @ApiIgnore
	@RequestMapping(value = "/google/login", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> googleLogin(HttpServletRequest pRequest, HttpServletResponse pResponse, Authentication authentication) {
		Map<String, Object> responseMap = new HashMap<>();

		if (authentication != null) {
			// 獲取google已驗證用戶的Principal
			Object principal = authentication.getPrincipal();
			String ip = getRemoteHost(pRequest);
			System.out.println(ip);
			// 獲取google用戶的屬性
			if (principal instanceof OAuth2User) {
				OAuth2User oAuth2User = (OAuth2User) principal;
				String oAuth2UserSub = (String) oAuth2User.getAttribute("sub");
				Optional<Member> optional = memberService.getDataByGoogleUid(oAuth2UserSub);
				Member memberAccountData = optional.orElse(null);
				// 專案已有帳號
				try {
					if (memberAccountData != null) {
						// session登錄
						responseMap.put("STATUS", "GLY");
						responseMap.put("MSG", "登入成功");
						responseMap.put("memberSession", memberAccountData);
					} else {
						responseMap.put("STATUS", "N");
						responseMap.put("MSG", "登入失敗");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		} else {
			responseMap.put("STATUS", "GLN");
		}
		return ResponseEntity.ok(responseMap);
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

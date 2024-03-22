package com.holidaydessert.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.holidaydessert.filter.SessionCookieFilter;
import com.holidaydessert.model.Member;
import com.holidaydessert.service.MemberService;

@SuppressWarnings("deprecation")
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private MemberService memberService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
				.antMatchers("/google/login").authenticated() // 指定需要Google登入驗證的頁面
				.anyRequest().permitAll() // 其他頁面允許訪問
				.and()
			.oauth2Login()
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest pRequest, HttpServletResponse pResponse,Authentication authentication) throws IOException, ServletException {
						Map<String, Object> responseMap = new HashMap<>();
						// 獲取google已驗證用戶的Principal
						Object principal = authentication.getPrincipal();
						String ip = getRemoteHost(pRequest);
						// 獲取HttpSession對象
						HttpSession session = pRequest.getSession();

						// 獲取google用戶的屬性
						if (principal instanceof OAuth2User) {
							OAuth2User oAuth2User = (OAuth2User) principal;
							String oAuth2UserSub = (String) oAuth2User.getAttribute("sub");
							String oAuth2UserName = (String) oAuth2User.getAttribute("name");
							String oAuth2UserEmail = (String) oAuth2User.getAttribute("email");
							
							Optional<Member> optional = memberService.getDataByGoogleUid(oAuth2UserSub);
							Member memberAccountData = optional.orElse(null);
							
							System.out.println(ip+"-"+session+"-"+oAuth2UserName+"-"+oAuth2UserEmail+"-"+oAuth2UserSub);
							// 專案已有帳號
							if (memberAccountData != null) {
			                    responseMap.put("STATUS", "Y");
			                    responseMap.put("MSG", "登入成功");
								responseMap.put("memberSession", memberAccountData);
							} else {
								// google
								Member member = new Member();
								member.setMemName(oAuth2UserName);
								member.setMemAccount(oAuth2UserEmail);
								member.setMemEmail(oAuth2UserEmail);
								member.setMemStatus("1");
								member.setMemVerificationStatus("1");
								member.setMemGoogleUid(oAuth2UserSub);
								member.setMemPassword("");
								memberService.register(member);
								
								// 註冊完馬上登入
								Optional<Member> getOptionalAfterRegister = memberService.getDataByGoogleUid(oAuth2UserSub);
								Member getDataAfterRegister = getOptionalAfterRegister.orElse(null);
								
			                    responseMap.put("STATUS", "Y");
			                    responseMap.put("MSG", "登入成功");
								responseMap.put("memberSession", getDataAfterRegister);
							}
							
						}
						pResponse.sendRedirect("/holidayDessert/front/login.html");
					}

				}).failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,AuthenticationException exception) throws IOException, ServletException {
						response.sendRedirect("/holidayDessert/front/index.html");
					}
				})
				.and()
				.addFilterAfter(new SessionCookieFilter(), BasicAuthenticationFilter.class).headers().xssProtection();
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

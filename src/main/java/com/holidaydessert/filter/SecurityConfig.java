package com.holidaydessert.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
					public void onAuthenticationSuccess(HttpServletRequest pRequest, HttpServletResponse pResponse,
							Authentication authentication) throws IOException, ServletException {
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
//							Optional<MemberAccount> optional = memberAccountService.getDataByGoogleUid(oAuth2UserSub);
//							MemberAccount memberAccountData = optional.orElse(null);

//							// 落點專案已有帳號
//							if (memberAccountData != null) {
//
//							} else {
//								// google
//							}

						}
						pResponse.sendRedirect("/forecast/index");
					}

				}).failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						response.sendRedirect("/forecast/index");
					}
				});

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

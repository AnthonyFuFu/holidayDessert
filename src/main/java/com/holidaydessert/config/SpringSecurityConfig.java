package com.holidaydessert.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.holidaydessert.filter.SessionCookieFilter;
import com.holidaydessert.model.Member;
import com.holidaydessert.service.MemberService;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private MemberService memberService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/front/google/login", "/front/google/logout")
                        .authenticated() // 指定需要Google登入驗證的頁面
                        .anyRequest().permitAll())
                .oauth2Login(login -> login
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest pRequest, HttpServletResponse pResponse, Authentication authentication) throws IOException, ServletException {
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

                                    System.out.println(ip + "-" + session + "-" + oAuth2UserName + "-" + oAuth2UserEmail + "-" + oAuth2UserSub);
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
                                pResponse.sendRedirect("/holidayDessert/index.html");
                            }

                        }).failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                        AuthenticationException exception) throws IOException, ServletException {
                        response.sendRedirect("/holidayDessert/index.html");
                    }
                }))
                .logout(logout -> logout
                        .logoutSuccessUrl("/holidayDessert/index.html") // 登出成功後跳轉的頁面
                        .logoutUrl("/front/google/logout") // 配置登出端點
                        .addLogoutHandler((request, response, auth) -> {
                            // 清理 HttpSession
                            HttpSession session = request.getSession(false);
                            if (session != null) {
                                session.invalidate();
                            }
                        })
                        .logoutSuccessHandler((request, response, auth) -> {
                            // 強制 Google 登出
                            String googleLogoutUrl = "https://accounts.google.com/logout";
                            response.sendRedirect(googleLogoutUrl);
                        })
                )
                .addFilterAfter(new SessionCookieFilter(), BasicAuthenticationFilter.class).headers(headers -> headers.xssProtection());

		return http.build();
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

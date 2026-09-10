package com.holidaydessert.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.crypto.SecretKey;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.constant.AllowedOrigin;
import com.holidaydessert.filter.OAuth2LoginOriginFilter;
import com.holidaydessert.filter.SessionCookieFilter;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Member;
import com.holidaydessert.service.MemberService;
import com.holidaydessert.utils.CommonUtil;

import io.jsonwebtoken.security.Keys;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private MemberService memberService;

	@Bean
	@Order(2)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()) // CORS 在 Spring Security 認證之前處理，header 才能加到 redirect response
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/front/google/login", "/front/google/logout")
                        .authenticated() // 指定需要Google登入驗證的頁面
                        .anyRequest().permitAll())
                .oauth2Login(login -> login
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest pRequest, HttpServletResponse pResponse, Authentication authentication) throws IOException, ServletException {
                                Object principal = authentication.getPrincipal();
                                String ip = CommonUtil.getRemoteHost(pRequest);
                                // 獲取HttpSession對象
                                HttpSession session = pRequest.getSession();

                                if (principal instanceof OAuth2User) {
                                    OAuth2User oAuth2User = (OAuth2User) principal;
                                    String oAuth2UserSub = (String) oAuth2User.getAttribute("sub");
                                    String oAuth2UserName = (String) oAuth2User.getAttribute("name");
                                    String oAuth2UserEmail = (String) oAuth2User.getAttribute("email");

                                    Optional<Member> optional = memberService.getDataByGoogleUid(oAuth2UserSub);
                                    log.info(ip + "-" + session + "-" + oAuth2UserName + "-" + oAuth2UserEmail + "-" + oAuth2UserSub);
                                    if (!optional.isPresent()) {
                                        Member member = new Member();
                                        member.setMemName(oAuth2UserName);
                                        member.setMemAccount(oAuth2UserEmail);
                                        member.setMemEmail(oAuth2UserEmail);
                                        member.setMemStatus("1");
                                        member.setMemVerificationStatus("1");
                                        member.setMemGoogleUid(oAuth2UserSub);
                                        member.setMemPassword("");
                                        memberService.register(member);
                                    }
                                }
                                // 加上 googleLogin=true，讓前端知道是 Google 登入
                                pResponse.sendRedirect(resolveRedirectPath(pRequest, "/holidayDessert/index") + "?googleLogin=true");
                                clearOriginPortCookie(pResponse);
                            }

                        }).failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        // 根據 port 決定是否加 .html
                        response.sendRedirect(
                            resolveRedirectPath(request, "/holidayDessert/index")
                        );
                        clearOriginPortCookie(response);
                    }
                }))
                .logout(logout -> logout
                        .logoutUrl("/front/google/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler((request, response, authentication) -> response.sendRedirect(resolveRedirectPath(request, "/holidayDessert/index"))
                    )
                )
                //這是真正的google登出方法
//              .logout(logout -> logout
//                      .logoutSuccessUrl("/holidayDessert/index.html") // 登出成功後跳轉的頁面
//                      .logoutUrl("/front/google/logout") // 配置登出端點
//                      .addLogoutHandler((request, response, auth) -> {
//                          // 清理 HttpSession
//                          HttpSession session = request.getSession(false);
//                          if (session != null) {
//                              session.invalidate();
//                          }
//                      })
//                      .logoutSuccessHandler((request, response, auth) -> {
//                          // 強制 Google 登出
//                          String googleLogoutUrl = "https://accounts.google.com/logout";
//                          response.sendRedirect(googleLogoutUrl);
//                      })
//              )
                .addFilterAfter(new SessionCookieFilter(), BasicAuthenticationFilter.class).headers(headers -> headers.xssProtection(Customizer.withDefaults()));

		return http.build();
	}
	
	private String resolveRedirectPath(HttpServletRequest request, String basePath) {
	    int port = getOriginPortFromCookie(request);
	    log.info("[resolveRedirectPath] 使用 port: " + port);
	    if (port == AllowedOrigin.PORT_HTML) {
	        return basePath + ".html";
	    } else {
	        return AllowedOrigin.OFFICAIL_VUE_HOST + basePath;
	    }
	}

	private int getOriginPortFromCookie(HttpServletRequest request) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (OAuth2LoginOriginFilter.COOKIE_KEY_ORIGIN_PORT.equals(cookie.getName())) {
	                try {
	                    int port = Integer.parseInt(cookie.getValue());
	                    return port;
	                } catch (NumberFormatException ignored) {}
	            }
	        }
	    }
	    return request.getLocalPort();
	}

	/** Cookie 用完就清掉，避免殘留 */
	private void clearOriginPortCookie(HttpServletResponse response) {
	    Cookie expiredCookie = new Cookie(OAuth2LoginOriginFilter.COOKIE_KEY_ORIGIN_PORT, "");
	    expiredCookie.setPath("/");
	    expiredCookie.setMaxAge(0); // 立即過期
	    response.addCookie(expiredCookie);
	}
	
	
	
	
	
	
	// JWT 驗證
	private static final String JWT_SECRET = "holiday-dessert-jwt-secret-key-very-secure";
	private static final String JWT_ISSUER = "holidaydesserAPIKey";
	@Autowired
	private ObjectMapper objectMapper;
	@Bean
	SecretKey jwtSecretKey() {
		return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
	}

	@Bean
	JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(jwtSecretKey).macAlgorithm(MacAlgorithm.HS256).build();
		decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(JWT_ISSUER));
		return decoder;
	}

	@Bean
	@Order(1)
	SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/api/**")
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exception -> exception
						// 沒有 Token、Token 無效、Token 過期
						.authenticationEntryPoint((request, response, authException) -> {
							ApiReturnObject result = ApiReturnObject.unauthorized("未登入或 Token 無效");
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
							response.getWriter().write(objectMapper.writeValueAsString(result));
						})
						// Token 有效，但權限不足
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							ApiReturnObject result = ApiReturnObject.forbidden("沒有權限存取此資源");
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
							response.getWriter().write(objectMapper.writeValueAsString(result));
						}))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "/api/token/getToken").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/token/refresh").permitAll()
						.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
						// Resource Server 驗證失敗時也使用相同 JSON
						.authenticationEntryPoint((request, response, authException) -> {
							ApiReturnObject result = ApiReturnObject.unauthorized("Token 無效或已過期");
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
							response.getWriter().write(objectMapper.writeValueAsString(result));
						})
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							ApiReturnObject result = ApiReturnObject.forbidden("沒有權限存取此資源");
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
							response.getWriter().write(objectMapper.writeValueAsString(result));
						}));
		return http.build();
	}
	
}

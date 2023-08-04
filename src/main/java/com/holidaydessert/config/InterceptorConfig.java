package com.holidaydessert.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.holidaydessert.interceptors.JWTInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	JWTInterceptor jwtInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor).addPathPatterns("/**") // 其他街口token驗證
				.excludePathPatterns("/user/login", "/swagger-ui.html",
						"/swagger-resources/**", "/asserts/**", "/webjars/**"); // 所有用戶都放行
	}
}
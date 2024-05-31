package com.holidaydessert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.holidaydessert.interceptors.LogInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	private final String[] excludePath = {"/static/**","/user/login", "/swagger-ui.html",
			"/swagger-resources/**", "/asserts/**", "/webjars/**"};

	@Bean
	public LogInterceptor logInterceptor() {
		return new LogInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor())
				.addPathPatterns("/**") // 其他街口token驗證
				.excludePathPatterns(excludePath); // 所有用戶都放行
	}
}
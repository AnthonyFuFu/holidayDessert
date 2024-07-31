package com.holidaydessert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
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
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**")		// 可限制哪个请求可以通过跨域
    		.allowedHeaders("*")  		// 可限制固定请求头可以通过跨域
    		.allowedMethods("*")		// 可限制固定methods可以通过跨域
    		.allowedOrigins("*")		// 可限制访问ip可以通过跨域
    		.allowCredentials(true)		// 是否允许发送cookie
    		.exposedHeaders(HttpHeaders.SET_COOKIE);
      }
    
}
package com.holidaydessert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.holidaydessert.constant.AllowedOrigin;

@Configuration
public class CorsConfig {

    // 改為 CorsConfigurationSource bean，讓 Spring Security 的 .cors() 在認證之前處理 CORS
    // 原本的 CorsFilter bean 執行順序在 Spring Security 之後，導致 redirect 時沒有 CORS header
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(AllowedOrigin.OFFICAIL);
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}

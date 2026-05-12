package com.holidaydessert.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${admin.upload.file.path}")
    private String ADMIN_UPLOAD_FILE_PATH;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 將 URL 路徑對應到實際檔案系統位置
        // 請求 /admin/upload/** 時，直接從檔案系統讀取
        registry.addResourceHandler("/admin/upload/**")
                .addResourceLocations("file:" + ADMIN_UPLOAD_FILE_PATH);
                
    }
    
}

package com.holidaydessert.config;

import java.util.Arrays;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
	
//	private final String[] includePath = {"/**/addSubmit","/**/updateSubmit","/**/editSubmit","/**/delete","/**/audit"};
	
	@Bean
	public Docket api() {
		/**
		 * clazz 存放想要過慮位於Swagger下方Model屬性類別
		 * apis:是Controller可以設定用package方式掃描否則會掃描全專案
		 * paths:路徑可以再做更進階的掃描只掃描某個Url底下的路由 build:建立Swagger
		 */
		@SuppressWarnings("rawtypes")
		Class[] clazz = { Accessible.class, AccessibleContext.class };
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(metaData()).select()
				.apis(RequestHandlerSelectors.basePackage("com.holidaydessert.controller"))
				.paths(PathSelectors.any()).build().enable(true).ignoredParameterTypes(clazz)
				.securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()));

	}
	/**
	 * 設定位於Swaager上方的敘述
	 * 
	 * @return
	 */
	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("HolidayDessert API 服務")
				.description("\"HolidayDessert API 服務\""
						+"\n"
						+"").version("springboot")
				.license("holidayDessert").licenseUrl("").build();
	}
	
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		
		registry.addResourceHandler("/configuration/ui", "/swagger-resources/configuration/ui")
				.addResourceLocations("classpath:/META-INF/resources/");
	}
	
	private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference("apiKey", new AuthorizationScope[0])))
                .build();
    }
    
    
    
    
    
    
    
    
    
    
    
    

//	@Bean
//	public Docket swaggerSetting() {
//		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//				.apis(RequestHandlerSelectors.basePackage("com.holidaydessert.controller")).paths(PathSelectors.any())
//				.build().securitySchemes(securitySchemes()).securityContexts(securityContexts());
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("SWAGGER測試環境").description("swagger test").version("1.0").build();
//	}
//
//	private List<SecurityScheme> securitySchemes() {
//		List<SecurityScheme> securitySchemes = new ArrayList<>();
//		securitySchemes.add(new ApiKey("Authorization", "Authorization", "header"));
//		return securitySchemes;
//	}
//
//	private List<SecurityContext> securityContexts() {
//		List<SecurityContext> securityContexts = new ArrayList<>();
//		securityContexts.add(SecurityContext.builder().securityReferences(defaultAuth())
//				.forPaths(PathSelectors.regex("^(?!auth).*$")).build());
//		return securityContexts;
//	}
//
//	private List<SecurityReference> defaultAuth() {
//		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//		authorizationScopes[0] = authorizationScope;
//		List<SecurityReference> securityReferences = new ArrayList<>();
//		securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
//		return securityReferences;
//	}

}
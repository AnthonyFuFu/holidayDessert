package com.holidaydessert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	/**
	 * springdoc 的 OpenAPI 描述（取代原本 springfox 的 Docket）
	 * 設定 API 標題/描述/版本，以及 apiKey（Authorization header）安全機制。
	 *
	 * 註：原本此類別 extends WebMvcConfigurationSupport 會關閉 Spring Boot MVC 自動設定，
	 * 導致 springdoc 的 /swagger-ui/** 無法載入，故改為單純 @Configuration，
	 * 靜態資源與 Swagger UI 交由 Boot 預設 + springdoc 自動處理。
	 */
	@Bean
	OpenAPI holidayDessertOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("HolidayDessert API 服務")
						.description("\"HolidayDessert API 服務\"\n")
						.version("springboot")
						.license(new License().name("holidayDessert").url("")))
				.components(new Components().addSecuritySchemes("apiKey",
						new SecurityScheme().type(SecurityScheme.Type.APIKEY)
								.in(SecurityScheme.In.HEADER).name("Authorization")))
				.addSecurityItem(new SecurityRequirement().addList("apiKey"));
	}

}

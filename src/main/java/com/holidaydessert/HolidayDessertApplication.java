package com.holidaydessert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.holidaydessert.repository")
public class HolidayDessertApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolidayDessertApplication.class, args);
	}

}

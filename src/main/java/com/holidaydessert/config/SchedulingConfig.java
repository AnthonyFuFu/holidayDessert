package com.holidaydessert.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling  // 啟用 @Scheduled（目前用於票務預熱自動關閉）
public class SchedulingConfig {
}

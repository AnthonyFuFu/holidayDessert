package com.holidaydessert.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RunSchedule {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//	private String apiUrl = "https://www.reallygood.com.tw/forecast/admin/exportReport/api/runAiEstimate";

//	@Scheduled(cron = "0 0 0 * * ?")
    public void run() {
		log.info("排程 Start time is {}", dateFormat.format(new Date()));

//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(apiUrl, String.class);
//        log.info("API response: " + response);
        
        log.info("排程 End time is {}", dateFormat.format(new Date()));
    }
	
}

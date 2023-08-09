package com.holidaydessert.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;

@Component
public class RunSchedule {

	private static final Logger log = LoggerFactory.getLogger(RunSchedule.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//	private String apiUrl = "https://www.reallygood.com.tw/forecast/admin/exportReport/api/runAiEstimate";

//	@Scheduled(cron = "0 0 0 * * ?")
    public void runAiEstimate() {
		log.info("AI排程 Start time is {}", dateFormat.format(new Date()));

//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(apiUrl, String.class);
//        System.out.println("API response: " + response);
        
        log.info("AI排程 End time is {}", dateFormat.format(new Date()));
    }
	
}

package com.holidaydessert.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.config.KafkaProducerConfig;
import com.holidaydessert.model.Member;

@RestController
@RequestMapping("kafka")
public class ProducerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerController.class);

	@Autowired(required = false) //如果有 KafkaTemplate 就幫我注入，沒有的話就塞 null，不要炸
	private KafkaTemplate<String, Member> kafkaTemplate;

	@PostMapping("/publish")
	public String publish(@RequestBody Member member) {
	    try {
	    	if (kafkaTemplate == null) {
	    	    return "Kafka is disabled";
	    	}
	        ListenableFuture<SendResult<String, Member>> future = kafkaTemplate.send(KafkaProducerConfig.JSON_TOPIC, member);
	        future.addCallback(new KafkaSendCallback<String, Member>() {
	            @Override
	            public void onSuccess(SendResult<String, Member> result) {
	                LOGGER.info("success send message:{} with offset:{} ", member, result.getRecordMetadata().offset());
	            }
	            @Override
	            public void onFailure(KafkaProducerException ex) {
	                LOGGER.error("fail send message! Do somthing....");
	            }
	        });
	        return "Published done";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Publish failed: " + e.getMessage();
	    }
	}

	@GetMapping("/publish/{memName}/{account}/{password}/{gender}/{phone}/{email}/{address}/{birthday}/{picture}/{image}")
	public String post(@PathVariable String memName, @PathVariable String account, @PathVariable String password,
			@PathVariable String gender, @PathVariable String phone, @PathVariable String email,
			@PathVariable String address, @PathVariable String birthday, @PathVariable String picture,
			@PathVariable String image) {
		Member member = new Member();
		member.setMemName(memName);
		member.setMemAccount(account);
		member.setMemPassword(password);
		member.setMemGender(gender);
		member.setMemPhone(phone);
		member.setMemEmail(email);
		member.setMemAddress(address);
		member.setMemBirthday(birthday);
		member.setMemPicture(picture);
		member.setMemImage(image);

		kafkaTemplate.send(KafkaProducerConfig.JSON_TOPIC, member);

		return "Published done";
	}
}

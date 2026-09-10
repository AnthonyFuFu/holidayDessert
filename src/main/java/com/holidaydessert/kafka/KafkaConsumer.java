package com.holidaydessert.kafka;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.holidaydessert.model.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("kafka")
public class KafkaConsumer {
	
    @KafkaListener(topics = KafkaConsumerConfig.TEST_TOPIC, groupId = KafkaConsumerConfig.GROUP_1)
    public void consume(String message) {
        log.info("Consumed message: {} ", message);
    }

    @KafkaListener(topics = KafkaConsumerConfig.JSON_TOPIC, groupId = KafkaConsumerConfig.GROUP_2,
            containerFactory = "memberKafkaListenerFactory")
    public void consumeJson(Member member) throws InterruptedException {
    	log.info("Consumed JSON Message: {} ", member);
    }
    
}

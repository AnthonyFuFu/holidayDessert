package com.holidaydessert.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.holidaydessert.config.KafkaConsumerConfig;
import com.holidaydessert.model.Member;

@Service
@Profile("kafka")
public class KafkaConsumer {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = KafkaConsumerConfig.TEST_TOPIC, groupId = KafkaConsumerConfig.GROUP_1)
    public void consume(String message) {
        LOGGER.info("Consumed message: {} ", message);
    }

    @KafkaListener(topics = KafkaConsumerConfig.JSON_TOPIC, groupId = KafkaConsumerConfig.GROUP_2,
            containerFactory = "memberKafkaListenerFactory")
    public void consumeJson(Member member) throws InterruptedException {
        LOGGER.info("Consumed JSON Message: {} ", member);
    }
    
}

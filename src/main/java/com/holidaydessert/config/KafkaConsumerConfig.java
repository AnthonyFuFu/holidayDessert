package com.holidaydessert.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import com.holidaydessert.controller.ProducerController;
import com.holidaydessert.model.Member;

@Configuration
@EnableKafka
@Profile("kafka")
public class KafkaConsumerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerController.class);

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	public static final String TEST_TOPIC = "test";
	public static final String JSON_TOPIC = "json";
	public static final String GROUP_1 = "group_1";
	public static final String GROUP_2 = "group_2";

	@Bean
	ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_1);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
	    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    factory.setCommonErrorHandler(kafkaErrorHandler());
		return factory;
	}
	
	@Bean
	ConsumerFactory<String, Member> memberConsumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_2);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		// use ErrorHandlingDeserializer
		ErrorHandlingDeserializer<Member> errorHandlingDeserializer = new ErrorHandlingDeserializer<>(new JsonDeserializer<>(Member.class));
		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
	}
	// @Bean(name = "memberKafkaListenerFactory") //會自動配置Bean 要與方法名稱要一樣才不用寫(name ="memberKafkaListenerFactory")
	@Bean
	ConcurrentKafkaListenerContainerFactory<String, Member> memberKafkaListenerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Member> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(memberConsumerFactory());
		// add ErrorHandler
	    factory.setCommonErrorHandler(kafkaErrorHandler());
		return factory;
	}
	@Bean
	DefaultErrorHandler kafkaErrorHandler() {
	    // 重試策略：每 3 秒一次，重試 3 次
	    FixedBackOff backOff = new FixedBackOff(3000L, 3L);
	    DefaultErrorHandler errorHandler = new DefaultErrorHandler(
	        (consumerRecord, exception) -> {
	            // recovery callback（等同你以前的 setRecoveryCallback）
	            LOGGER.info("Recovery is called for message: {}", consumerRecord.value()
	            );
	        },
	        backOff
	    );
	    return errorHandler;
	}
}

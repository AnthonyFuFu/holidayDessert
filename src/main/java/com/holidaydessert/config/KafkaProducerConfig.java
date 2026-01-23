//package com.holidaydessert.config;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.ProducerFactory;
//
//import com.holidaydessert.model.Ticket;
//
//import io.confluent.kafka.serializers.KafkaAvroSerializer;
//
//@Configuration
//@EnableKafka
//public class KafkaProducerConfig {
//	@Value("${kafka.bootstrap-servers}")
//	private String bootstrapServers;
//
//	@Value("${kafka.properties.security.protocol}")
//	private String securityProtocol;
//
//	@Value("${kafka.properties.sasl.mechanism}")
//	private String saslMechanism;
//
//	@Value("${kafka.producer.password}")
//	private String password;
//
//	@Value("${kafka.producer.username}")
//	private String username;
//
//	@Value("${kafka.properties.schema.registry.url}")
//	private String schemaRegistryUrl;
//
//	@Bean
//	ProducerFactory<String, Ticket> producerFactory() {
//		Map<String, Object> configProps = new HashMap<>();
//		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
//		configProps.put(ProducerConfig.RETRIES_CONFIG, 1);
//		configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//		configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//		configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1024000);
//		configProps.put("security.protocol", securityProtocol);
//		configProps.put("sasl.mechanism", saslMechanism);
//		configProps.put("sasl.jaas.config",
//				"org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + username
//						+ "\" password=\"" + password + "\";");
//		configProps.put("schema.registry.url", schemaRegistryUrl);
//
//		return new DefaultKafkaProducerFactory<>(configProps);
//	}
//
//}
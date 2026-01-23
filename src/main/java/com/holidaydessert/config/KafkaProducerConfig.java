package com.holidaydessert.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.holidaydessert.model.Member;

@Configuration
@EnableKafka
@Profile("kafka")
public class KafkaProducerConfig {

	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Value("${kafka.properties.security.protocol}")
	private String securityProtocol;

	@Value("${kafka.properties.sasl.mechanism}")
	private String saslMechanism;

	@Value("${kafka.producer.password}")
	private String password;

	@Value("${kafka.producer.username}")
	private String username;

	@Value("${kafka.properties.schema.registry.url}")
	private String schemaRegistryUrl;

    public static final String JSON_TOPIC = "json";

    @Bean
    ProducerFactory<String, Member> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        config.put(ProducerConfig.RETRIES_CONFIG, 1);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1024000);
        
        // config.put("security.protocol", securityProtocol);
        // config.put("sasl.mechanism", saslMechanism);
        // config.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + username + "\" password=\"" + password + "\";");
        // config.put("schema.registry.url", schemaRegistryUrl);
		
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    KafkaTemplate<String, Member> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    NewTopic defaultTopic() {
        return TopicBuilder.name(JSON_TOPIC).build();
    }
}

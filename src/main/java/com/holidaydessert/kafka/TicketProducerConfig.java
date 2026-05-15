package com.holidaydessert.kafka;

import com.holidaydessert.model.TicketOrderMessageDto;
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

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Profile("kafka")
public class TicketProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static final String TICKET_ORDER_TOPIC = "ticket-order";

    @Bean
    ProducerFactory<String, TicketOrderMessageDto> ticketOrderProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");   // ✅ 確保 Leader + Follower 都收到才確認
        config.put(ProducerConfig.RETRIES_CONFIG, 3);    // 失敗重試 3 次
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1024000);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    KafkaTemplate<String, TicketOrderMessageDto> ticketOrderKafkaTemplate() {
        return new KafkaTemplate<>(ticketOrderProducerFactory());
    }

    @Bean
    NewTopic ticketOrderTopic() {
        return TopicBuilder.name(TICKET_ORDER_TOPIC)
                .partitions(3)  // 3 個分區，提高並行消費能力
                .replicas(1)    // 學習環境單節點
                .build();
    }
}
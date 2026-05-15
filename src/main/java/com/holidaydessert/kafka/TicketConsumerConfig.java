package com.holidaydessert.kafka;

import com.holidaydessert.model.TicketOrderMessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
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

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
@Profile("kafka")
public class TicketConsumerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    public static final String TICKET_ORDER_GROUP = "ticket-order-group";

    @Bean
    ConsumerFactory<String, TicketOrderMessageDto> ticketOrderConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, TICKET_ORDER_GROUP);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        // ✅ 與你原本的 Member 寫法一致，用 ErrorHandlingDeserializer 包裝
        ErrorHandlingDeserializer<TicketOrderMessageDto> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(TicketOrderMessageDto.class));

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TicketOrderMessageDto> ticketOrderKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TicketOrderMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ticketOrderConsumerFactory());
        factory.setCommonErrorHandler(ticketOrderErrorHandler());
        return factory;
    }

    @Bean
    DefaultErrorHandler ticketOrderErrorHandler() {
        // 每 3 秒重試一次，最多重試 3 次（與你原本的寫法一致）
        FixedBackOff backOff = new FixedBackOff(3000L, 3L);
        return new DefaultErrorHandler(
            (consumerRecord, exception) -> {
                // 3 次重試耗盡後的最終處理
                log.error("[Kafka] ❌ 重試耗盡，訂單持久化失敗 message={} ex={}",
                        consumerRecord.value(), exception.getMessage());
                // TODO: 寫入補償表或推告警
            },
            backOff
        );
    }
    
}

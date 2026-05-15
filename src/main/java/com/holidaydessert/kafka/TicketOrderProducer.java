package com.holidaydessert.kafka;

import com.holidaydessert.model.TicketOrderMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Profile("kafka")
public class TicketOrderProducer {

    @Autowired
    private KafkaTemplate<String, TicketOrderMessageDto> ticketOrderKafkaTemplate;

    /**
     * 將搶票成功的訂單資訊送入 Kafka
     * Key 使用 typeId，確保同一票種的訂單進入同一 Partition（有序消費）
     */
    public void sendOrder(Integer typeId, Integer memId, Integer quantity, Integer price) {
    	TicketOrderMessageDto message = TicketOrderMessageDto.builder()
                .typeId(typeId)
                .memId(memId)
                .quantity(quantity)
                .price(price)
                .build();
    	
    	// Spring Kafka 3.x
        // ✅ 新版 Spring Kafka 用 CompletableFuture 取代舊的 ListenableFuture
        CompletableFuture<SendResult<String, TicketOrderMessageDto>> future =
                ticketOrderKafkaTemplate.send(
                        TicketProducerConfig.TICKET_ORDER_TOPIC,
                        String.valueOf(typeId), // ← Partition Key
                        message
                ).completable(); 

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("[Kafka] ✅ 訂單訊息發送成功 typeId={} memId={} offset={}",
                        typeId, memId, result.getRecordMetadata().offset());
            } else {
                log.error("[Kafka] ❌ 訂單訊息發送失敗 typeId={} memId={}", typeId, memId, ex);
                // TODO: 降級寫入補償表
            }
        });
    }
}
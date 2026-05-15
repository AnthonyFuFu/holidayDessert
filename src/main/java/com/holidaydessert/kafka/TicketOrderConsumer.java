package com.holidaydessert.kafka;

import com.holidaydessert.constant.TicketConstant;
import com.holidaydessert.model.TicketOrder;
import com.holidaydessert.model.TicketOrderMessageDto;
import com.holidaydessert.repository.MemberRepository;
import com.holidaydessert.repository.TicketOrderRepository;
import com.holidaydessert.repository.TicketTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Profile("kafka")
public class TicketOrderConsumer {

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketOrderRepository ticketOrderRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 消費 ticket-order Topic，寫入 DB
     * 與原本 persistOrder() 邏輯完全相同，但由 Kafka 驅動
     */
    @KafkaListener(
            topics  = TicketProducerConfig.TICKET_ORDER_TOPIC,
            groupId = TicketConsumerConfig.TICKET_ORDER_GROUP,
            containerFactory = "ticketOrderKafkaListenerFactory"
    )
    @Transactional
    public void consumeOrder(TicketOrderMessageDto message) {
        log.info("[Kafka] 收到訂單訊息 typeId={} memId={} qty={}",
                message.getTypeId(), message.getMemId(), message.getQuantity());
        try {
            // Step 1：扣減 DB 庫存
            int affected = ticketTypeRepository.decrementRemaining(
                    message.getTypeId(), message.getQuantity());

            if (affected == 0) {
                log.warn("[Kafka] ⚠️ DB 庫存不足 typeId={} memId={}",
                        message.getTypeId(), message.getMemId());
                return;
            }

            // Step 2：建立訂單
            TicketOrder order = new TicketOrder();
            order.setMember(memberRepository.getReferenceById(message.getMemId()));
            order.setTicketType(ticketTypeRepository.getReferenceById(message.getTypeId()));
            order.setTicketOrdQuantity(message.getQuantity());
            order.setTicketOrdStatus(0);
            order.setTicketOrdAmount(message.getPrice() * message.getQuantity());
            order.setTicketOrdExpire(
                    LocalDateTime.now().plusMinutes(TicketConstant.ORDER_EXPIRE_MINUTES));

            ticketOrderRepository.save(order);

            log.info("[Kafka] ✅ 訂單寫入完成 typeId={} memId={} amount={}",
                    message.getTypeId(), message.getMemId(),
                    message.getPrice() * message.getQuantity());

        } catch (Exception e) {
            // 拋出例外讓 Kafka ErrorHandler 觸發重試
            log.error("[Kafka] ❌ 訂單寫入失敗 typeId={} memId={}",
                    message.getTypeId(), message.getMemId(), e);
            throw e;
        }
    }
}
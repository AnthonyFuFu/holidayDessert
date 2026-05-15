package com.holidaydessert.service;

import com.holidaydessert.constant.TicketConstant;
import com.holidaydessert.model.TicketOrder;
import com.holidaydessert.repository.MemberRepository;
import com.holidaydessert.repository.TicketOrderRepository;
import com.holidaydessert.repository.TicketTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TicketOrderService {

	@Autowired
	private TicketTypeRepository ticketTypeRepository;

	@Autowired
	private TicketOrderRepository ticketOrderRepository;

	@Autowired
	private MemberRepository memberRepository;

	/**
	 * 異步寫入 DB：扣減庫存 & 建立訂單（同一事務，失敗全部 rollback）
	 *
	 * @param typeId   票種 ID
	 * @param memId    會員 ID
	 * @param quantity 購買張數
	 * @param price    票種單價（快照，來自 Redis）
	 */
	@Async("dbWriteExecutor")
	@Transactional
	public void persistOrder(Integer typeId, Integer memId, Integer quantity, Integer price) {
		try {
			// Step 1：扣減 DB 庫存（有防超賣條件，返回 0 代表庫存不足）
			int affected = ticketTypeRepository.decrementRemaining(typeId, quantity);
			if (affected == 0) {
				log.warn("[DB] ⚠️ DB 庫存不足 typeId={} memId={}", typeId, memId);
				// 最低限度：記錄補償日誌，供人工處理
				saveCompensationLog(typeId, memId, quantity, "DB_STOCK_INSUFFICIENT");
				return;
			}

			// Step 2：建立訂單（getReferenceById 只設 FK，不額外查詢）
			TicketOrder order = new TicketOrder();
			order.setMember(memberRepository.getReferenceById(memId));
			order.setTicketType(ticketTypeRepository.getReferenceById(typeId));
			order.setTicketOrdQuantity(quantity);
			order.setTicketOrdStatus(0); // 0: 待付款
			order.setTicketOrdAmount(price * quantity); // 金額快照
			order.setTicketOrdExpire(LocalDateTime.now().plusMinutes(TicketConstant.ORDER_EXPIRE_MINUTES)); // 付款期限
			// ticketOrdPayTime 為 null（尚未付款）

			ticketOrderRepository.save(order);

			log.info("[DB] ✅ 訂單建立完成 typeId={} memId={} amount={}", typeId, memId, price * quantity);

		} catch (Exception e) {
			log.error("[DB] ❌ 持久化失敗 typeId={} memId={}", typeId, memId, e);
			saveCompensationLog(typeId, memId, quantity, "EXCEPTION: " + e.getMessage());
			// TODO: 推入 MQ 重試
		}
	}

	// 先用 DB Table 記錄補償需求，再非同步處理
	private void saveCompensationLog(Integer typeId, Integer memId, Integer quantity, String reason) {
		// 寫入 ticket_compensation_log 表（需另建）
		log.error("[Compensation] 需人工補償 typeId={} memId={} qty={} reason={}", typeId, memId, quantity, reason);
	}
	
}

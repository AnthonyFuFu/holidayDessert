package com.holidaydessert.schedule;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.holidaydessert.model.Ticket;
import com.holidaydessert.repository.TicketRepository;
import com.holidaydessert.service.TicketService;

import lombok.extern.slf4j.Slf4j;

/**
 * 票務預熱自動關閉：每分鐘檢查已預熱的活動，符合以下任一條件即關閉預熱
 * 1. 已預熱的票種全部售完
 * 2. 超過售票結束時間
 * 3. 活動狀態為「活動結束」或「活動取消」
 */
@Slf4j
@Component
public class TicketWarmUpSchedule {

	private static final int STATUS_ENDED = 2;
	private static final int STATUS_CANCELLED = 3;

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private TicketService ticketService;

	@Scheduled(initialDelay = 60_000, fixedDelay = 60_000)
	public void closeFinishedWarmUps() {
		LocalDateTime now = LocalDateTime.now();
		for (Ticket ticket : ticketRepository.findAll()) {
			try {
				int remaining = ticketService.warmedRemaining(ticket.getTicketId());
				if (remaining < 0) {
					continue; // 未預熱
				}
				String reason = null;
				if (remaining == 0) {
					reason = "全部售完";
				} else if (ticket.getTicketSaleEnd() != null && ticket.getTicketSaleEnd().isBefore(now)) {
					reason = "售票時間結束";
				} else if (ticket.getTicketStatus() != null && ticket.getTicketStatus() == STATUS_ENDED) {
					reason = "活動結束";
				} else if (ticket.getTicketStatus() != null && ticket.getTicketStatus() == STATUS_CANCELLED) {
					reason = "活動取消";
				}
				if (reason != null) {
					log.info("[Schedule] {}", ticketService.closeWarmUp(ticket.getTicketId(), "自動關閉：" + reason));
				}
			} catch (Exception e) {
				log.error("[Schedule] 預熱自動關閉失敗 ticketId={}", ticket.getTicketId(), e);
			}
		}
	}

}

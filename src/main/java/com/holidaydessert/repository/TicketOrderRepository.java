package com.holidaydessert.repository;

import com.holidaydessert.model.TicketOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketOrderRepository extends JpaRepository<TicketOrder, Integer> {
}
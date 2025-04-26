package com.holidaydessert.service;

import com.holidaydessert.model.Ticket;

public interface TicketRedisService {
	
	public String purchaseTicket(String event);
    public void saveToRedis(Ticket ticket);
    public Ticket getFromRedis(Long id);
    public void deleteFromRedis(Long id);
    
}

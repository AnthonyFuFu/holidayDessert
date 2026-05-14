package com.holidaydessert.service;

public interface TicketSeckillService {
	
	String initTickets(String event, int ticketCount);
	String purchaseTicket(String event, String userId);
	int remainCount(String event);
	
}
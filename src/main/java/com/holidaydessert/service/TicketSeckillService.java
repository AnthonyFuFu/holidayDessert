package com.holidaydessert.service;

public interface TicketSeckillService {
	
	String initTickets(String event, int ticketCount);
	String purchaseTicket(String ticketName, Integer userId);
	int remainCount(String ticketName);
	
}
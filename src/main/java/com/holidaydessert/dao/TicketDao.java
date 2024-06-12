package com.holidaydessert.dao;

import com.holidaydessert.model.Ticket;

public interface TicketDao {

    public Ticket findByEvent(String event);
    public void save(Ticket ticket);
    
}

package com.holidaydessert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
	
    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestParam String event) {
        String response = ticketService.purchaseTicket(event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}

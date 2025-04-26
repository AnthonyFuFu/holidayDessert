package com.holidaydessert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.Ticket;
import com.holidaydessert.service.TicketRedisService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
	
    @Autowired
    private TicketRedisService ticketRedisService;
    
    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestParam String event) {
        String response = ticketRedisService.purchaseTicket(event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/cache")
    public ResponseEntity<?> saveTicket(@RequestBody Ticket ticket) {
        ticketRedisService.saveToRedis(ticket);
        return ResponseEntity.ok("Saved to Redis");
    }

    @GetMapping("/cache/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketRedisService.getFromRedis(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping("/cache/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketRedisService.deleteFromRedis(id);
        return ResponseEntity.ok("Deleted from Redis");
    }
    
}

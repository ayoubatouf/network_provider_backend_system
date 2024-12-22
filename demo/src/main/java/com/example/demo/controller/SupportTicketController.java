package com.example.demo.controller;

import com.example.demo.model.SupportTicket;
import com.example.demo.model.User;
import com.example.demo.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/support-tickets")
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @PostMapping
    public ResponseEntity<SupportTicket> createOrUpdateSupportTicket(@RequestBody SupportTicket supportTicket) {
        SupportTicket savedSupportTicket = supportTicketService.saveSupportTicket(supportTicket);
        return new ResponseEntity<>(savedSupportTicket, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllSupportTickets() {
        List<SupportTicket> supportTickets = supportTicketService.getAllSupportTickets();
        return new ResponseEntity<>(supportTickets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getSupportTicketById(@PathVariable Long id) {
        Optional<SupportTicket> supportTicket = supportTicketService.getSupportTicketById(id);
        return supportTicket.map(st -> new ResponseEntity<>(st, HttpStatus.OK))
                            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupportTicket(@PathVariable Long id) {
        supportTicketService.deleteSupportTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupportTicket> updateSupportTicket(@PathVariable Long id, @RequestBody SupportTicket supportTicket) {
        try {
            SupportTicket updatedTicket = supportTicketService.updateSupportTicket(id, supportTicket);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SupportTicket>> getSupportTicketsByStatus(@PathVariable String status) {
        List<SupportTicket> tickets = supportTicketService.getSupportTicketsByStatus(status);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicket>> getSupportTicketsByUser(@PathVariable Long userId) {
        User user = new User();  
        user.setId(userId);
        List<SupportTicket> tickets = supportTicketService.getSupportTicketsByUser(user);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countSupportTicketsByStatus(@PathVariable String status) {
        long count = supportTicketService.countSupportTicketsByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/created-between")
    public ResponseEntity<List<SupportTicket>> getSupportTicketsByCreatedDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SupportTicket> tickets = supportTicketService.getSupportTicketsByCreatedDateRange(startDate, endDate);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupportTicket>> searchSupportTicketsByIssueDescription(@RequestParam("keyword") String keyword) {
        List<SupportTicket> tickets = supportTicketService.searchSupportTicketsByIssueDescription(keyword);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}

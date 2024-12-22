package com.example.demo.service;

import com.example.demo.model.SupportTicket;
import com.example.demo.model.User;
import com.example.demo.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;

    @Autowired
    public SupportTicketService(SupportTicketRepository supportTicketRepository) {
        this.supportTicketRepository = supportTicketRepository;
    }

    public SupportTicket saveSupportTicket(SupportTicket supportTicket) {
        return supportTicketRepository.save(supportTicket);
    }

    public List<SupportTicket> getAllSupportTickets() {
        return supportTicketRepository.findAll();
    }

    public Optional<SupportTicket> getSupportTicketById(Long id) {
        return supportTicketRepository.findById(id);
    }

    public void deleteSupportTicket(Long id) {
        supportTicketRepository.deleteById(id);
    }

    public SupportTicket updateSupportTicket(Long id, SupportTicket updatedTicket) {
        return supportTicketRepository.findById(id)
                .map(ticket -> {
                    ticket.setIssueDescription(updatedTicket.getIssueDescription());
                    ticket.setStatus(updatedTicket.getStatus());
                    ticket.setLastModifiedDate(LocalDateTime.now());
                    return supportTicketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Support ticket not found"));
    }

    public List<SupportTicket> getSupportTicketsByStatus(String status) {
        return supportTicketRepository.findByStatus(status);
    }

    public List<SupportTicket> getSupportTicketsByUser(User user) {
        return supportTicketRepository.findByUser(user);
    }

    public long countSupportTicketsByStatus(String status) {
        return supportTicketRepository.countByStatus(status);
    }

    public List<SupportTicket> getSupportTicketsByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return supportTicketRepository.findByCreatedDateBetween(startDate, endDate);
    }

    public List<SupportTicket> searchSupportTicketsByIssueDescription(String keyword) {
        return supportTicketRepository.findByIssueDescriptionContainingIgnoreCase(keyword);
    }

}

package com.example.demo.repository;

import com.example.demo.model.SupportTicket;
import com.example.demo.model.User;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

    List<SupportTicket> findByIssueDescriptionContainingIgnoreCase(String keyword);
    long countByStatus(String status);
    List<SupportTicket> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<SupportTicket> findByStatus(String status);
    List<SupportTicket> findByUser(User user);
   
}

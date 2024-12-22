package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.model.User;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Payment> findByOrder(Order order);
    List<Payment> findByUser(User user);
    List<Payment> findByAmountBetween(Double minAmount, Double maxAmount);
    
}

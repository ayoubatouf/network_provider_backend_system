package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public List<Payment> getPaymentsByUser(User user) {
        return paymentRepository.findByUser(user);
    }

    public List<Payment> getPaymentsByOrder(Order order) {
        return paymentRepository.findByOrder(order);
    }

    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByPaymentDateBetween(startDate, endDate);
    }

    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment existingPayment = paymentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        existingPayment.setAmount(paymentDetails.getAmount());
        existingPayment.setPaymentDate(paymentDetails.getPaymentDate());
        existingPayment.setUser(paymentDetails.getUser());
        existingPayment.setOrder(paymentDetails.getOrder());

        return paymentRepository.save(existingPayment);
    }

    public boolean doesPaymentExist(Long id) {
        return paymentRepository.existsById(id);
    }

    public List<Payment> getPaymentsByAmountRange(Double minAmount, Double maxAmount) {
        return paymentRepository.findByAmountBetween(minAmount, maxAmount);
    }
}

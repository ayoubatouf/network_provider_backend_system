package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createOrUpdatePayment(@RequestBody Payment payment) {
        Payment savedPayment = paymentService.savePayment(payment);
        return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);  
        List<Payment> payments = paymentService.getPaymentsByUser(user);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsByOrder(@PathVariable Long orderId) {
        Order order = new Order();
        order.setId(orderId);  
        List<Payment> payments = paymentService.getPaymentsByOrder(order);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Payment> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        Payment updatedPayment = paymentService.updatePayment(id, paymentDetails);
        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> doesPaymentExist(@PathVariable Long id) {
        boolean exists = paymentService.doesPaymentExist(id);
        return exists ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/amount-range")
    public ResponseEntity<List<Payment>> getPaymentsByAmountRange(
            @RequestParam Double minAmount,
            @RequestParam Double maxAmount) {
        List<Payment> payments = paymentService.getPaymentsByAmountRange(minAmount, maxAmount);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
}

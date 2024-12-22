package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrUpdateOrder(@RequestBody Order order) {
        Order savedOrder = orderService.saveOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(o -> new ResponseEntity<>(o, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        try {
            Order updated = orderService.updateOrder(id, updatedOrder);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        User user = new User(); 
        user.setId(userId);
        List<Order> orders = orderService.getOrdersByUser(user);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/service-plan/{servicePlanId}")
    public ResponseEntity<List<Order>> getOrdersByServicePlan(@PathVariable Long servicePlanId) {
        ServicePlan servicePlan = new ServicePlan(); 
        servicePlan.setId(servicePlanId);
        List<Order> orders = orderService.getOrdersByServicePlan(servicePlan);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> calculateTotalOrderAmountForUser(@PathVariable Long userId) {
        User user = new User(); 
        user.setId(userId);
        Double totalAmount = orderService.calculateTotalOrderAmountForUser(user);
        return new ResponseEntity<>(totalAmount, HttpStatus.OK);
    }

    @PostMapping("/{orderId}/payments")
    public ResponseEntity<Order> addPaymentToOrder(@PathVariable Long orderId, @RequestBody Payment payment) {
        Order updatedOrder = orderService.addPaymentToOrder(orderId, payment);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/payments/{paymentId}")
    public ResponseEntity<Order> removePaymentFromOrder(@PathVariable Long orderId, @PathVariable Long paymentId) {
        Order updatedOrder = orderService.removePaymentFromOrder(orderId, paymentId);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}

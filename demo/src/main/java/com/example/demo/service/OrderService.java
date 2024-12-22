package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setOrderDate(updatedOrder.getOrderDate());
                    order.setTotalAmount(updatedOrder.getTotalAmount());
                    order.setUser(updatedOrder.getUser());
                    order.setServicePlan(updatedOrder.getServicePlan());
                    order.setPayments(updatedOrder.getPayments());
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getOrdersByServicePlan(ServicePlan servicePlan) {
        return orderRepository.findByServicePlan(servicePlan);
    }

    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    public Double calculateTotalOrderAmountForUser(User user) {
        return orderRepository.findByUser(user).stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    public Order addPaymentToOrder(Long orderId, Payment payment) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.getPayments().add(payment);
                    payment.setOrder(order);  
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
    }

    public Order removePaymentFromOrder(Long orderId, Long paymentId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    Payment payment = order.getPayments().stream()
                            .filter(p -> p.getId().equals(paymentId))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Payment not found with id " + paymentId));
                    order.getPayments().remove(payment);
                    payment.setOrder(null);  
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));
    }

}


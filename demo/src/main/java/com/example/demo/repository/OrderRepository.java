package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
    List<Order> findByServicePlan(ServicePlan servicePlan);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  
}


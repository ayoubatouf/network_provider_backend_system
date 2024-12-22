package com.example.demo.repository;

import com.example.demo.model.ServicePlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePlanRepository extends JpaRepository<ServicePlan, Long> {

    List<ServicePlan> findByNameContainingOrDescriptionContaining(String query, String query2);
    long countUsersInServicePlan(Long servicePlanId);
    long countOrdersInServicePlan(Long servicePlanId);

}

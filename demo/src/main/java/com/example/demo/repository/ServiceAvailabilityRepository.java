package com.example.demo.repository;

import com.example.demo.model.ServiceAvailability;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAvailabilityRepository extends JpaRepository<ServiceAvailability, Long> {

    List<ServiceAvailability> findByAvailabilityStatus(String status);
    List<ServiceAvailability> findByAvailabilityDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    void deleteByAvailabilityStatus(String status);

}
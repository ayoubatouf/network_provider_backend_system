package com.example.demo.repository;

import com.example.demo.model.NetworkStatus;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NetworkStatusRepository extends JpaRepository<NetworkStatus, Long> {

    List<NetworkStatus> findByRegionId(Long regionId);
    List<NetworkStatus> findByUpdateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<NetworkStatus> findByServiceAvailabilitiesId(Long serviceAvailabilityId);
    List<NetworkStatus> findAllById(List<Long> ids);
}

package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.NetworkStatus;
import com.example.demo.repository.NetworkStatusRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NetworkStatusService {

    private final NetworkStatusRepository networkStatusRepository;

    @Autowired
    public NetworkStatusService(NetworkStatusRepository networkStatusRepository) {
        this.networkStatusRepository = networkStatusRepository;
    }

    public NetworkStatus saveNetworkStatus(NetworkStatus networkStatus) {
        return networkStatusRepository.save(networkStatus);
    }

    public List<NetworkStatus> getAllNetworkStatuses() {
        return networkStatusRepository.findAll();
    }

    public Optional<NetworkStatus> getNetworkStatusById(Long id) {
        return networkStatusRepository.findById(id);
    }

    public void deleteNetworkStatus(Long id) {
        networkStatusRepository.deleteById(id);
    }

    public NetworkStatus updateNetworkStatus(Long id, NetworkStatus networkStatus) {
        Optional<NetworkStatus> existingNetworkStatus = networkStatusRepository.findById(id);
        if (existingNetworkStatus.isPresent()) {
            NetworkStatus existing = existingNetworkStatus.get();
            existing.setStatus(networkStatus.getStatus());
            existing.setUpdateDate(networkStatus.getUpdateDate());
            existing.setRegion(networkStatus.getRegion());
            existing.setServiceAvailabilities(networkStatus.getServiceAvailabilities());
            return networkStatusRepository.save(existing);
        }
        throw new ResourceNotFoundException("NetworkStatus with id " + id + " not found");
    }

    public List<NetworkStatus> getNetworkStatusesByRegion(Long regionId) {
        return networkStatusRepository.findByRegionId(regionId);
    }

    public List<NetworkStatus> getNetworkStatusesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return networkStatusRepository.findByUpdateDateBetween(startDate, endDate);
    }

    public List<NetworkStatus> getNetworkStatusesByServiceAvailability(Long serviceAvailabilityId) {
        return networkStatusRepository.findByServiceAvailabilitiesId(serviceAvailabilityId);
    }

    @Transactional
    public void bulkUpdateNetworkStatuses(List<Long> ids, String newStatus) {
        List<NetworkStatus> networkStatuses = networkStatusRepository.findAllById(ids);
        for (NetworkStatus networkStatus : networkStatuses) {
            networkStatus.setStatus(newStatus);
        }
        networkStatusRepository.saveAll(networkStatuses);
    }
}

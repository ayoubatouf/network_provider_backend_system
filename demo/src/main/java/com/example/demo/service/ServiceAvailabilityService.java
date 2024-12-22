package com.example.demo.service;

import com.example.demo.model.NetworkStatus;
import com.example.demo.model.ServiceAvailability;
import com.example.demo.model.ServicePlan;
import com.example.demo.repository.ServiceAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceAvailabilityService {

    private final ServiceAvailabilityRepository serviceAvailabilityRepository;

    @Autowired
    public ServiceAvailabilityService(ServiceAvailabilityRepository serviceAvailabilityRepository) {
        this.serviceAvailabilityRepository = serviceAvailabilityRepository;
    }

    public ServiceAvailability saveServiceAvailability(ServiceAvailability serviceAvailability) {
        return serviceAvailabilityRepository.save(serviceAvailability);
    }

    public List<ServiceAvailability> getAllServiceAvailabilities() {
        return serviceAvailabilityRepository.findAll();
    }

    public Optional<ServiceAvailability> getServiceAvailabilityById(Long id) {
        return serviceAvailabilityRepository.findById(id);
    }

    public ServiceAvailability updateServiceAvailability(Long id, ServiceAvailability serviceAvailability) {
        return serviceAvailabilityRepository.findById(id)
            .map(existingServiceAvailability -> {
                existingServiceAvailability.setAvailabilityStatus(serviceAvailability.getAvailabilityStatus());
                existingServiceAvailability.setAvailabilityDate(serviceAvailability.getAvailabilityDate());
                existingServiceAvailability.setServicePlans(serviceAvailability.getServicePlans());
                existingServiceAvailability.setNetworkStatuses(serviceAvailability.getNetworkStatuses());
                return serviceAvailabilityRepository.save(existingServiceAvailability);
            })
            .orElseThrow(() -> new RuntimeException("ServiceAvailability not found with id " + id));
    }

    public void deleteServiceAvailability(Long id) {
        serviceAvailabilityRepository.deleteById(id);
    }

    public List<ServiceAvailability> getServiceAvailabilitiesByStatus(String status) {
        return serviceAvailabilityRepository.findByAvailabilityStatus(status);
    }

    public List<ServiceAvailability> getServiceAvailabilitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return serviceAvailabilityRepository.findByAvailabilityDateBetween(startDate, endDate);
    }

    public long getServiceAvailabilityCount() {
        return serviceAvailabilityRepository.count();
    }

    public void deleteServiceAvailabilitiesByStatus(String status) {
        serviceAvailabilityRepository.deleteByAvailabilityStatus(status);
    }

    public ServiceAvailability addServicePlanToServiceAvailability(Long serviceAvailabilityId, ServicePlan servicePlan) {
        ServiceAvailability serviceAvailability = serviceAvailabilityRepository.findById(serviceAvailabilityId)
            .orElseThrow(() -> new RuntimeException("ServiceAvailability not found with id " + serviceAvailabilityId));
        serviceAvailability.getServicePlans().add(servicePlan);
        return serviceAvailabilityRepository.save(serviceAvailability);
    }

    public ServiceAvailability addNetworkStatusToServiceAvailability(Long serviceAvailabilityId, NetworkStatus networkStatus) {
        ServiceAvailability serviceAvailability = serviceAvailabilityRepository.findById(serviceAvailabilityId)
            .orElseThrow(() -> new RuntimeException("ServiceAvailability not found with id " + serviceAvailabilityId));
        serviceAvailability.getNetworkStatuses().add(networkStatus);
        return serviceAvailabilityRepository.save(serviceAvailability);
    }

    public List<ServiceAvailability> getAllServiceAvailabilitiesSortedByDate() {
        return serviceAvailabilityRepository.findAll(Sort.by(Sort.Order.asc("availabilityDate")));
    }
}


package com.example.demo.controller;

import com.example.demo.model.NetworkStatus;
import com.example.demo.model.ServiceAvailability;
import com.example.demo.model.ServicePlan;
import com.example.demo.service.ServiceAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/service-availabilities")
public class ServiceAvailabilityController {

    private final ServiceAvailabilityService serviceAvailabilityService;

    @Autowired
    public ServiceAvailabilityController(ServiceAvailabilityService serviceAvailabilityService) {
        this.serviceAvailabilityService = serviceAvailabilityService;
    }

    @PostMapping
    public ResponseEntity<ServiceAvailability> createOrUpdateServiceAvailability(@RequestBody ServiceAvailability serviceAvailability) {
        ServiceAvailability savedServiceAvailability = serviceAvailabilityService.saveServiceAvailability(serviceAvailability);
        return new ResponseEntity<>(savedServiceAvailability, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServiceAvailability>> getAllServiceAvailabilities() {
        List<ServiceAvailability> serviceAvailabilities = serviceAvailabilityService.getAllServiceAvailabilities();
        return new ResponseEntity<>(serviceAvailabilities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceAvailability> getServiceAvailabilityById(@PathVariable Long id) {
        Optional<ServiceAvailability> serviceAvailability = serviceAvailabilityService.getServiceAvailabilityById(id);
        return serviceAvailability.map(sa -> new ResponseEntity<>(sa, HttpStatus.OK))
                                  .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceAvailability> updateServiceAvailability(@PathVariable Long id, @RequestBody ServiceAvailability serviceAvailability) {
        try {
            ServiceAvailability updatedServiceAvailability = serviceAvailabilityService.updateServiceAvailability(id, serviceAvailability);
            return new ResponseEntity<>(updatedServiceAvailability, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceAvailability(@PathVariable Long id) {
        serviceAvailabilityService.deleteServiceAvailability(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ServiceAvailability>> getServiceAvailabilitiesByStatus(@PathVariable String status) {
        List<ServiceAvailability> serviceAvailabilities = serviceAvailabilityService.getServiceAvailabilitiesByStatus(status);
        return new ResponseEntity<>(serviceAvailabilities, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ServiceAvailability>> getServiceAvailabilitiesByDateRange(@RequestParam("start") String startDate, @RequestParam("end") String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate);
            LocalDateTime end = LocalDateTime.parse(endDate);
            List<ServiceAvailability> serviceAvailabilities = serviceAvailabilityService.getServiceAvailabilitiesByDateRange(start, end);
            return new ResponseEntity<>(serviceAvailabilities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/service-plans")
    public ResponseEntity<ServiceAvailability> addServicePlanToServiceAvailability(@PathVariable Long id, @RequestBody ServicePlan servicePlan) {
        try {
            ServiceAvailability updatedServiceAvailability = serviceAvailabilityService.addServicePlanToServiceAvailability(id, servicePlan);
            return new ResponseEntity<>(updatedServiceAvailability, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/network-statuses")
    public ResponseEntity<ServiceAvailability> addNetworkStatusToServiceAvailability(@PathVariable Long id, @RequestBody NetworkStatus networkStatus) {
        try {
            ServiceAvailability updatedServiceAvailability = serviceAvailabilityService.addNetworkStatusToServiceAvailability(id, networkStatus);
            return new ResponseEntity<>(updatedServiceAvailability, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getServiceAvailabilityCount() {
        long count = serviceAvailabilityService.getServiceAvailabilityCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @DeleteMapping("/status/{status}")
    public ResponseEntity<Void> deleteServiceAvailabilitiesByStatus(@PathVariable String status) {
        serviceAvailabilityService.deleteServiceAvailabilitiesByStatus(status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.NetworkStatus;
import com.example.demo.service.NetworkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/network-statuses")
public class NetworkStatusController {

    private final NetworkStatusService networkStatusService;

    @Autowired
    public NetworkStatusController(NetworkStatusService networkStatusService) {
        this.networkStatusService = networkStatusService;
    }

    @PostMapping
    public ResponseEntity<NetworkStatus> createOrUpdateNetworkStatus(@RequestBody NetworkStatus networkStatus) {
        NetworkStatus savedNetworkStatus = networkStatusService.saveNetworkStatus(networkStatus);
        return new ResponseEntity<>(savedNetworkStatus, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NetworkStatus>> getAllNetworkStatuses() {
        List<NetworkStatus> networkStatuses = networkStatusService.getAllNetworkStatuses();
        return new ResponseEntity<>(networkStatuses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NetworkStatus> getNetworkStatusById(@PathVariable Long id) {
        Optional<NetworkStatus> networkStatus = networkStatusService.getNetworkStatusById(id);
        return networkStatus.map(ns -> new ResponseEntity<>(ns, HttpStatus.OK))
                            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNetworkStatus(@PathVariable Long id) {
        networkStatusService.deleteNetworkStatus(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NetworkStatus> updateNetworkStatus(@PathVariable Long id, @RequestBody NetworkStatus networkStatus) {
        try {
            NetworkStatus updatedNetworkStatus = networkStatusService.updateNetworkStatus(id, networkStatus);
            return new ResponseEntity<>(updatedNetworkStatus, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<NetworkStatus>> getNetworkStatusesByRegion(@PathVariable Long regionId) {
        List<NetworkStatus> networkStatuses = networkStatusService.getNetworkStatusesByRegion(regionId);
        return new ResponseEntity<>(networkStatuses, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NetworkStatus>> getNetworkStatusesByDateRange(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<NetworkStatus> networkStatuses = networkStatusService.getNetworkStatusesByDateRange(startDate, endDate);
        return new ResponseEntity<>(networkStatuses, HttpStatus.OK);
    }

    @GetMapping("/service-availability/{serviceAvailabilityId}")
    public ResponseEntity<List<NetworkStatus>> getNetworkStatusesByServiceAvailability(@PathVariable Long serviceAvailabilityId) {
        List<NetworkStatus> networkStatuses = networkStatusService.getNetworkStatusesByServiceAvailability(serviceAvailabilityId);
        return new ResponseEntity<>(networkStatuses, HttpStatus.OK);
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<Void> bulkUpdateNetworkStatuses(
        @RequestParam List<Long> ids, @RequestParam String newStatus) {
        networkStatusService.bulkUpdateNetworkStatuses(ids, newStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

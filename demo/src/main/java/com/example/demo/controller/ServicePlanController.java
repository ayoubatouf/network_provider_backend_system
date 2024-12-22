package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Feedback;
import com.example.demo.model.ServicePlan;
import com.example.demo.service.ServicePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/service-plans")
public class ServicePlanController {

    private final ServicePlanService servicePlanService;

    @Autowired
    public ServicePlanController(ServicePlanService servicePlanService) {
        this.servicePlanService = servicePlanService;
    }

    @PostMapping
    public ResponseEntity<ServicePlan> createOrUpdateServicePlan(@RequestBody ServicePlan servicePlan) {
        ServicePlan savedServicePlan = servicePlanService.saveServicePlan(servicePlan);
        return new ResponseEntity<>(savedServicePlan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServicePlan>> getAllServicePlans() {
        List<ServicePlan> servicePlans = servicePlanService.getAllServicePlans();
        return new ResponseEntity<>(servicePlans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePlan> getServicePlanById(@PathVariable Long id) {
        Optional<ServicePlan> servicePlan = servicePlanService.getServicePlanById(id);
        return servicePlan.map(sp -> new ResponseEntity<>(sp, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicePlan(@PathVariable Long id) {
        servicePlanService.deleteServicePlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePlan> updateServicePlan(@PathVariable Long id, @RequestBody ServicePlan servicePlan) {
        try {
            ServicePlan updatedServicePlan = servicePlanService.updateServicePlan(id, servicePlan);
            return new ResponseEntity<>(updatedServicePlan, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{servicePlanId}/users/{userId}")
    public ResponseEntity<ServicePlan> addUserToServicePlan(@PathVariable Long servicePlanId, @PathVariable Long userId) {
        try {
            ServicePlan updatedServicePlan = servicePlanService.addUserToServicePlan(servicePlanId, userId);
            return new ResponseEntity<>(updatedServicePlan, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{servicePlanId}/users/{userId}")
    public ResponseEntity<ServicePlan> removeUserFromServicePlan(@PathVariable Long servicePlanId, @PathVariable Long userId) {
        try {
            ServicePlan updatedServicePlan = servicePlanService.removeUserFromServicePlan(servicePlanId, userId);
            return new ResponseEntity<>(updatedServicePlan, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{servicePlanId}/availabilities/{availabilityId}")
    public ResponseEntity<ServicePlan> addServiceAvailabilityToServicePlan(@PathVariable Long servicePlanId, @PathVariable Long availabilityId) {
        try {
            ServicePlan updatedServicePlan = servicePlanService.addServiceAvailabilityToServicePlan(servicePlanId, availabilityId);
            return new ResponseEntity<>(updatedServicePlan, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{servicePlanId}/availabilities/{availabilityId}")
    public ResponseEntity<ServicePlan> removeServiceAvailabilityFromServicePlan(@PathVariable Long servicePlanId, @PathVariable Long availabilityId) {
        try {
            ServicePlan updatedServicePlan = servicePlanService.removeServiceAvailabilityFromServicePlan(servicePlanId, availabilityId);
            return new ResponseEntity<>(updatedServicePlan, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ServicePlan>> searchServicePlans(@RequestParam String query) {
        List<ServicePlan> servicePlans = servicePlanService.searchServicePlans(query);
        return new ResponseEntity<>(servicePlans, HttpStatus.OK);
    }

    @GetMapping("/{id}/users/count")
    public ResponseEntity<Long> countUsersInServicePlan(@PathVariable Long id) {
        long count = servicePlanService.countUsersInServicePlan(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/{id}/orders/count")
    public ResponseEntity<Long> countOrdersInServicePlan(@PathVariable Long id) {
        long count = servicePlanService.countOrdersInServicePlan(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/{id}/feedbacks")
    public ResponseEntity<List<Feedback>> getFeedbacksForServicePlan(@PathVariable Long id) {
        try {
            List<Feedback> feedbacks = servicePlanService.getFeedbacksForServicePlan(id);
            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Feedback;
import com.example.demo.model.ServiceAvailability;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.repository.ServiceAvailabilityRepository;
import com.example.demo.repository.ServicePlanRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicePlanService {

    private final ServicePlanRepository servicePlanRepository;
    private final UserRepository userRepository;
    private final ServiceAvailabilityRepository serviceAvailabilityRepository;

    @Autowired
    public ServicePlanService(ServicePlanRepository servicePlanRepository,
                              UserRepository userRepository,
                              ServiceAvailabilityRepository serviceAvailabilityRepository) {
        this.servicePlanRepository = servicePlanRepository;
        this.userRepository = userRepository;
        this.serviceAvailabilityRepository = serviceAvailabilityRepository;
    }

    public ServicePlan saveServicePlan(ServicePlan servicePlan) {
        return servicePlanRepository.save(servicePlan);
    }

    public List<ServicePlan> getAllServicePlans() {
        return servicePlanRepository.findAll();
    }

    public Optional<ServicePlan> getServicePlanById(Long id) {
        return servicePlanRepository.findById(id);
    }

    public void deleteServicePlan(Long id) {
        servicePlanRepository.deleteById(id);
    }

    public ServicePlan updateServicePlan(Long id, ServicePlan updatedServicePlan) {
        Optional<ServicePlan> existingServicePlanOpt = servicePlanRepository.findById(id);
        if (existingServicePlanOpt.isPresent()) {
            ServicePlan existingServicePlan = existingServicePlanOpt.get();
            existingServicePlan.setName(updatedServicePlan.getName());
            existingServicePlan.setDescription(updatedServicePlan.getDescription());
            existingServicePlan.setServiceAvailabilities(updatedServicePlan.getServiceAvailabilities());
            return servicePlanRepository.save(existingServicePlan);
        } else {
            throw new ResourceNotFoundException("ServicePlan with id " + id + " not found.");
        }
    }

    public ServicePlan addUserToServicePlan(Long servicePlanId, Long userId) {
        Optional<ServicePlan> servicePlanOpt = servicePlanRepository.findById(servicePlanId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (servicePlanOpt.isPresent() && userOpt.isPresent()) {
            ServicePlan servicePlan = servicePlanOpt.get();
            User user = userOpt.get();
            servicePlan.getUsers().add(user);
            user.getServicePlans().add(servicePlan); 
            servicePlanRepository.save(servicePlan);
            return servicePlan;
        } else {
            throw new ResourceNotFoundException("ServicePlan or User not found.");
        }
    }

    public ServicePlan removeUserFromServicePlan(Long servicePlanId, Long userId) {
        Optional<ServicePlan> servicePlanOpt = servicePlanRepository.findById(servicePlanId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (servicePlanOpt.isPresent() && userOpt.isPresent()) {
            ServicePlan servicePlan = servicePlanOpt.get();
            User user = userOpt.get();
            servicePlan.getUsers().remove(user);
            user.getServicePlans().remove(servicePlan); 
            servicePlanRepository.save(servicePlan);
            return servicePlan;
        } else {
            throw new ResourceNotFoundException("ServicePlan or User not found.");
        }
    }

    public ServicePlan addServiceAvailabilityToServicePlan(Long servicePlanId, Long serviceAvailabilityId) {
        Optional<ServicePlan> servicePlanOpt = servicePlanRepository.findById(servicePlanId);
        Optional<ServiceAvailability> serviceAvailabilityOpt = serviceAvailabilityRepository.findById(serviceAvailabilityId);

        if (servicePlanOpt.isPresent() && serviceAvailabilityOpt.isPresent()) {
            ServicePlan servicePlan = servicePlanOpt.get();
            ServiceAvailability serviceAvailability = serviceAvailabilityOpt.get();
            servicePlan.getServiceAvailabilities().add(serviceAvailability);
            servicePlanRepository.save(servicePlan);
            return servicePlan;
        } else {
            throw new ResourceNotFoundException("ServicePlan or ServiceAvailability not found.");
        }
    }

    public ServicePlan removeServiceAvailabilityFromServicePlan(Long servicePlanId, Long serviceAvailabilityId) {
        Optional<ServicePlan> servicePlanOpt = servicePlanRepository.findById(servicePlanId);
        Optional<ServiceAvailability> serviceAvailabilityOpt = serviceAvailabilityRepository.findById(serviceAvailabilityId);

        if (servicePlanOpt.isPresent() && serviceAvailabilityOpt.isPresent()) {
            ServicePlan servicePlan = servicePlanOpt.get();
            ServiceAvailability serviceAvailability = serviceAvailabilityOpt.get();
            servicePlan.getServiceAvailabilities().remove(serviceAvailability);
            servicePlanRepository.save(servicePlan);
            return servicePlan;
        } else {
            throw new ResourceNotFoundException("ServicePlan or ServiceAvailability not found.");
        }
    }

    public List<ServicePlan> searchServicePlans(String query) {
        return servicePlanRepository.findByNameContainingOrDescriptionContaining(query, query);
    }

    public long countUsersInServicePlan(Long servicePlanId) {
        return servicePlanRepository.countUsersInServicePlan(servicePlanId);
    }

    public long countOrdersInServicePlan(Long servicePlanId) {
        return servicePlanRepository.countOrdersInServicePlan(servicePlanId);
    }

    public List<Feedback> getFeedbacksForServicePlan(Long servicePlanId) {
        Optional<ServicePlan> servicePlanOpt = servicePlanRepository.findById(servicePlanId);
        if (servicePlanOpt.isPresent()) {
            return servicePlanOpt.get().getFeedbacks();
        } else {
            throw new ResourceNotFoundException("ServicePlan with id " + servicePlanId + " not found.");
        }
    }
}


package com.example.demo.service;

import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.Role;
import com.example.demo.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());
            user.setRegion(userDetails.getRegion());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    public List<User> searchUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User assignServicePlan(Long userId, ServicePlan servicePlan) {
        return userRepository.findById(userId).map(user -> {
            user.getServicePlans().add(servicePlan);
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public User removeServicePlan(Long userId, ServicePlan servicePlan) {
        return userRepository.findById(userId).map(user -> {
            user.getServicePlans().remove(servicePlan);
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public Set<ServicePlan> getServicePlansForUser(Long userId) {
        return userRepository.findById(userId)
                .map(User::getServicePlans)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}

package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.security.Role;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createOrUpdateUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @GetMapping("/search/username")
    public ResponseEntity<List<User>> searchUsersByUsername(@RequestParam String username) {
        List<User> users = userService.searchUsersByUsername(username);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/search/role")
    public ResponseEntity<List<User>> searchUsersByRole(@RequestParam Role role) {
        List<User> users = userService.searchUsersByRole(role);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/{id}/service-plans")
    public ResponseEntity<User> assignServicePlan(@PathVariable Long id, @RequestBody ServicePlan servicePlan) {
        try {
            User user = userService.assignServicePlan(id, servicePlan);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/service-plans")
    public ResponseEntity<User> removeServicePlan(@PathVariable Long id, @RequestBody ServicePlan servicePlan) {
        try {
            User user = userService.removeServicePlan(id, servicePlan);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/service-plans")
    public ResponseEntity<Set<ServicePlan>> getServicePlansForUser(@PathVariable Long id) {
        try {
            Set<ServicePlan> servicePlans = userService.getServicePlansForUser(id);
            return new ResponseEntity<>(servicePlans, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

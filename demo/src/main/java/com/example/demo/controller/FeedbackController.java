package com.example.demo.controller;

import com.example.demo.model.Feedback;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Feedback> createOrUpdateFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback updatedFeedback) {
        Feedback feedback = feedbackService.updateFeedback(id, updatedFeedback);
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        return feedback.map(f -> new ResponseEntity<>(f, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<Feedback>> getFeedbacksByRating(@PathVariable int rating) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByRating(rating);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @GetMapping("/rating-range")
    public ResponseEntity<List<Feedback>> getFeedbacksByRatingRange(@RequestParam int min, @RequestParam int max) {
        List<Feedback> feedbacks = feedbackService.getFeedbacksByRatingRange(min, max);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByUser(@PathVariable Long userId) {
        User user = new User();  
        user.setId(userId);
        List<Feedback> feedbacks = feedbackService.getFeedbacksByUser(user);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @GetMapping("/service-plan/{servicePlanId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByServicePlan(@PathVariable Long servicePlanId) {
        ServicePlan servicePlan = new ServicePlan();  
        servicePlan.setId(servicePlanId);
        List<Feedback> feedbacks = feedbackService.getFeedbacksByServicePlan(servicePlan);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Feedback>> getPagedFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PageRequest.of(page, size, 
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Feedback> feedbacks = feedbackService.getFeedbacks(pageable);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }
}


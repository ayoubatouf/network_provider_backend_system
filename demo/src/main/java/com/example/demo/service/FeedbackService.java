package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Feedback;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.repository.FeedbackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback saveFeedback(Feedback feedback) {
        validateFeedback(feedback);
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    feedback.setFeedbackText(updatedFeedback.getFeedbackText());
                    feedback.setRating(updatedFeedback.getRating());
                    feedback.setSubmittedDate(updatedFeedback.getSubmittedDate());
                    feedback.setUser(updatedFeedback.getUser());
                    feedback.setServicePlan(updatedFeedback.getServicePlan());
                    validateFeedback(feedback);
                    return feedbackRepository.save(feedback);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Feedback with id " + id + " not found."));
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }

    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback with id " + id + " not found.");
        }
        feedbackRepository.deleteById(id);
    }

    public List<Feedback> getFeedbacksByRating(int rating) {
        return feedbackRepository.findAllByRating(rating);
    }

    public List<Feedback> getFeedbacksByRatingRange(int minRating, int maxRating) {
        return feedbackRepository.findAllByRatingBetween(minRating, maxRating);
    }

    public List<Feedback> getFeedbacksByUser(User user) {
        return feedbackRepository.findAllByUser(user);
    }

    public List<Feedback> getFeedbacksByServicePlan(ServicePlan servicePlan) {
        return feedbackRepository.findAllByServicePlan(servicePlan);
    }

    public Page<Feedback> getFeedbacks(Pageable pageable) {
        return feedbackRepository.findAll(pageable);
    }

    private void validateFeedback(Feedback feedback) {
        if (feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (feedback.getFeedbackText() == null || feedback.getFeedbackText().length() < 5 || feedback.getFeedbackText().length() > 500) {
            throw new IllegalArgumentException("Feedback text must be between 5 and 500 characters.");
        }
    }

}

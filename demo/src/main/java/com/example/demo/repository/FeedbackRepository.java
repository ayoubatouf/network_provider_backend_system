package com.example.demo.repository;

import com.example.demo.model.Feedback;
import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByRating(int rating);
    List<Feedback> findAllByRatingBetween(int minRating, int maxRating);
    List<Feedback> findAllByUser(User user);
    List<Feedback> findAllByServicePlan(ServicePlan servicePlan);
}

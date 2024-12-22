package com.example.demo.controller;

import com.example.demo.model.Feedback;
import com.example.demo.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


@WebMvcTest(FeedbackController.class)
@ActiveProfiles("test")
class FeedbackControllerTest {

    @MockBean
    private FeedbackService feedbackService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FeedbackController(feedbackService)).build();
        objectMapper = new ObjectMapper();
        
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    void testCreateOrUpdateFeedback() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setFeedbackText("Great service!");
        feedback.setRating(5);
        feedback.setSubmittedDate(LocalDateTime.of(2023, 12, 18, 14, 30));

        when(feedbackService.saveFeedback(any(Feedback.class))).thenReturn(feedback);

        mockMvc.perform(post("/api/feedbacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.feedbackText").value("Great service!"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.submittedDate").value("2023-12-18T14:30:00"));

        verify(feedbackService, times(1)).saveFeedback(any(Feedback.class));
    }

    @Test
    void testUpdateFeedback() throws Exception {
        Feedback updatedFeedback = new Feedback();
        updatedFeedback.setFeedbackText("Updated feedback");
        updatedFeedback.setRating(4);
        updatedFeedback.setSubmittedDate(LocalDateTime.of(2023, 12, 18, 15, 30));

        when(feedbackService.updateFeedback(eq(1L), any(Feedback.class))).thenReturn(updatedFeedback);

        mockMvc.perform(put("/api/feedbacks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFeedback)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackText").value("Updated feedback"))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.submittedDate").value("2023-12-18T15:30:00"));

        verify(feedbackService, times(1)).updateFeedback(eq(1L), any(Feedback.class));
    }

    @Test
    void testGetAllFeedbacks() throws Exception {
        Feedback feedback1 = new Feedback();
        feedback1.setFeedbackText("Great service!");
        feedback1.setRating(5);
        feedback1.setSubmittedDate(LocalDateTime.of(2023, 12, 18, 14, 30));

        Feedback feedback2 = new Feedback();
        feedback2.setFeedbackText("Good, but could improve.");
        feedback2.setRating(3);
        feedback2.setSubmittedDate(LocalDateTime.of(2023, 12, 18, 14, 30));

        when(feedbackService.getAllFeedbacks()).thenReturn(Arrays.asList(feedback1, feedback2));

        mockMvc.perform(get("/api/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedbackText").value("Great service!"))
                .andExpect(jsonPath("$[1].feedbackText").value("Good, but could improve."));

        verify(feedbackService, times(1)).getAllFeedbacks();
    }

    @Test
    void testGetFeedbackById() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setFeedbackText("Great service!");
        feedback.setRating(5);
        feedback.setSubmittedDate(LocalDateTime.of(2023, 12, 18, 14, 30));

        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.of(feedback));

        mockMvc.perform(get("/api/feedbacks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackText").value("Great service!"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.submittedDate").value("2023-12-18T14:30:00"));

        verify(feedbackService, times(1)).getFeedbackById(1L);
    }

    @Test
    void testGetFeedbackById_NotFound() throws Exception {
        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/feedbacks/1"))
                .andExpect(status().isNotFound());

        verify(feedbackService, times(1)).getFeedbackById(1L);
    }

    @Test
    void testDeleteFeedback() throws Exception {
        doNothing().when(feedbackService).deleteFeedback(1L);

        mockMvc.perform(delete("/api/feedbacks/1"))
                .andExpect(status().isNoContent());

        verify(feedbackService, times(1)).deleteFeedback(1L);
    }

    @Test
    void testGetFeedbacksByRating() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setFeedbackText("Great service!");
        feedback.setRating(5);
        feedback.setSubmittedDate(LocalDateTime.of(2023, 12, 18, 14, 30));

        when(feedbackService.getFeedbacksByRating(5)).thenReturn(Collections.singletonList(feedback));

        mockMvc.perform(get("/api/feedbacks/rating/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedbackText").value("Great service!"))
                .andExpect(jsonPath("$[0].rating").value(5));

        verify(feedbackService, times(1)).getFeedbacksByRating(5);
    }

    @Test
    void testGetFeedbacksByRatingRange() throws Exception {
        Feedback feedback = new Feedback();
        feedback.setFeedbackText("Good service!");
        feedback.setRating(4);
        feedback.setSubmittedDate(LocalDateTime.of(2023, 12, 18, 14, 30));

        when(feedbackService.getFeedbacksByRatingRange(3, 5)).thenReturn(Collections.singletonList(feedback));

        mockMvc.perform(get("/api/feedbacks/rating-range?min=3&max=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedbackText").value("Good service!"))
                .andExpect(jsonPath("$[0].rating").value(4));

        verify(feedbackService, times(1)).getFeedbacksByRatingRange(3, 5);
    }
}

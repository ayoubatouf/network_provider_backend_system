package com.example.demo.controller;

import com.example.demo.model.Payment;
import com.example.demo.service.PaymentService;
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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(PaymentController.class)
@ActiveProfiles("test")
class PaymentControllerTest {

    @MockBean
    private PaymentService paymentService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(paymentService)).build();
        objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));

    }

    @Test
    void testCreateOrUpdatePayment() throws Exception {
        Payment payment = new Payment();
        payment.setAmount(250.0);
        payment.setPaymentDate(LocalDateTime.of(2023, 12, 17, 14, 30));  

        when(paymentService.savePayment(any(Payment.class))).thenReturn(payment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(250.0))
                .andExpect(jsonPath("$.paymentDate").value("2023-12-17T14:30:00"));  
        verify(paymentService, times(1)).savePayment(any(Payment.class));
    }

    @Test
    void testGetAllPayments() throws Exception {
        Payment payment1 = new Payment();
        payment1.setAmount(250.0);
        payment1.setPaymentDate(LocalDateTime.of(2023, 12, 17, 14, 30));  

        Payment payment2 = new Payment();
        payment2.setAmount(150.0);
        payment2.setPaymentDate(LocalDateTime.of(2023, 12, 16, 10, 15));  

        when(paymentService.getAllPayments()).thenReturn(Arrays.asList(payment1, payment2));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(250.0))
                .andExpect(jsonPath("$[0].paymentDate").value("2023-12-17T14:30:00"))  
                .andExpect(jsonPath("$[1].amount").value(150.0))
                .andExpect(jsonPath("$[1].paymentDate").value("2023-12-16T10:15:00"));  

        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testGetPaymentById() throws Exception {
        Payment payment = new Payment();
        payment.setAmount(250.0);
        payment.setPaymentDate(LocalDateTime.of(2023, 12, 17, 14, 30));  

        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(250.0))
                .andExpect(jsonPath("$.paymentDate").value("2023-12-17T14:30:00"));  

        verify(paymentService, times(1)).getPaymentById(1L);
    }

    @Test
    void testGetPaymentById_NotFound() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isNotFound());

        verify(paymentService, times(1)).getPaymentById(1L);
    }

    @Test
    void testDeletePayment() throws Exception {
        doNothing().when(paymentService).deletePayment(1L);

        mockMvc.perform(delete("/api/payments/1"))
                .andExpect(status().isNoContent());

        verify(paymentService, times(1)).deletePayment(1L);
    }
}

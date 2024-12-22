package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
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

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService)).build();
        objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    void testCreateOrUpdateOrder() throws Exception {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.of(2023, 12, 17, 14, 30));
        order.setTotalAmount(100.0);

        when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderDate").value("2023-12-17T14:30:00"))
                .andExpect(jsonPath("$.totalAmount").value(100.0));

        verify(orderService, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void testGetAllOrders() throws Exception {
        Order order1 = new Order();
        order1.setOrderDate(LocalDateTime.of(2023, 12, 17, 14, 30));
        order1.setTotalAmount(100.0);

        Order order2 = new Order();
        order2.setOrderDate(LocalDateTime.of(2023, 12, 16, 10, 15));
        order2.setTotalAmount(200.0);

        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderDate").value("2023-12-17T14:30:00"))
                .andExpect(jsonPath("$[1].orderDate").value("2023-12-16T10:15:00"));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById() throws Exception {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.of(2023, 12, 17, 14, 30));
        order.setTotalAmount(100.0);

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderDate").value("2023-12-17T14:30:00"))
                .andExpect(jsonPath("$.totalAmount").value(100.0));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(1L);
    }
}

package com.example.demo.controller;

import com.example.demo.model.ServiceAvailability;
import com.example.demo.service.ServiceAvailabilityService;
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

@WebMvcTest(ServiceAvailabilityController.class)
@ActiveProfiles("test")
class ServiceAvailabilityControllerTest {

    @MockBean
    private ServiceAvailabilityService serviceAvailabilityService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ServiceAvailabilityController(serviceAvailabilityService)).build();
        objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    void testCreateOrUpdateServiceAvailability() throws Exception {
        ServiceAvailability serviceAvailability = new ServiceAvailability();
        serviceAvailability.setAvailabilityStatus("Available");
        serviceAvailability.setAvailabilityDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(serviceAvailabilityService.saveServiceAvailability(any(ServiceAvailability.class)))
                .thenReturn(serviceAvailability);

        mockMvc.perform(post("/api/service-availabilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceAvailability)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.availabilityStatus").value("Available"))
                .andExpect(jsonPath("$.availabilityDate").value("2023-12-17T14:30:00"));

        verify(serviceAvailabilityService, times(1)).saveServiceAvailability(any(ServiceAvailability.class));
    }

    @Test
    void testGetAllServiceAvailabilities() throws Exception {
        ServiceAvailability serviceAvailability1 = new ServiceAvailability();
        serviceAvailability1.setAvailabilityStatus("Available");
        serviceAvailability1.setAvailabilityDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        ServiceAvailability serviceAvailability2 = new ServiceAvailability();
        serviceAvailability2.setAvailabilityStatus("Not Available");
        serviceAvailability2.setAvailabilityDate(LocalDateTime.of(2023, 12, 18, 9, 0));

        when(serviceAvailabilityService.getAllServiceAvailabilities()).thenReturn(Arrays.asList(serviceAvailability1, serviceAvailability2));

        mockMvc.perform(get("/api/service-availabilities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].availabilityStatus").value("Available"))
                .andExpect(jsonPath("$[1].availabilityStatus").value("Not Available"))
                .andExpect(jsonPath("$[0].availabilityDate").value("2023-12-17T14:30:00"))
                .andExpect(jsonPath("$[1].availabilityDate").value("2023-12-18T09:00:00"));

        verify(serviceAvailabilityService, times(1)).getAllServiceAvailabilities();
    }

    @Test
    void testGetServiceAvailabilityById() throws Exception {
        ServiceAvailability serviceAvailability = new ServiceAvailability();
        serviceAvailability.setAvailabilityStatus("Available");
        serviceAvailability.setAvailabilityDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(serviceAvailabilityService.getServiceAvailabilityById(1L)).thenReturn(Optional.of(serviceAvailability));

        mockMvc.perform(get("/api/service-availabilities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availabilityStatus").value("Available"))
                .andExpect(jsonPath("$.availabilityDate").value("2023-12-17T14:30:00"));

        verify(serviceAvailabilityService, times(1)).getServiceAvailabilityById(1L);
    }

    @Test
    void testGetServiceAvailabilityById_NotFound() throws Exception {
        when(serviceAvailabilityService.getServiceAvailabilityById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/service-availabilities/1"))
                .andExpect(status().isNotFound());

        verify(serviceAvailabilityService, times(1)).getServiceAvailabilityById(1L);
    }



    @Test
    void testGetServiceAvailabilitiesByStatus() throws Exception {
        ServiceAvailability serviceAvailability = new ServiceAvailability();
        serviceAvailability.setAvailabilityStatus("Available");
        serviceAvailability.setAvailabilityDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(serviceAvailabilityService.getServiceAvailabilitiesByStatus("Available"))
                .thenReturn(Arrays.asList(serviceAvailability));

        mockMvc.perform(get("/api/service-availabilities/status/Available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].availabilityStatus").value("Available"))
                .andExpect(jsonPath("$[0].availabilityDate").value("2023-12-17T14:30:00"));

        verify(serviceAvailabilityService, times(1)).getServiceAvailabilitiesByStatus("Available");
    }

    @Test
    void testGetServiceAvailabilitiesByDateRange() throws Exception {
        ServiceAvailability serviceAvailability = new ServiceAvailability();
        serviceAvailability.setAvailabilityStatus("Available");
        serviceAvailability.setAvailabilityDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(serviceAvailabilityService.getServiceAvailabilitiesByDateRange(any(), any()))
                .thenReturn(Arrays.asList(serviceAvailability));

        mockMvc.perform(get("/api/service-availabilities/date-range")
                        .param("start", "2023-12-17T00:00:00")
                        .param("end", "2023-12-17T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].availabilityStatus").value("Available"))
                .andExpect(jsonPath("$[0].availabilityDate").value("2023-12-17T14:30:00"));

        verify(serviceAvailabilityService, times(1)).getServiceAvailabilitiesByDateRange(any(), any());
    }


    @Test
    void testGetServiceAvailabilityCount() throws Exception {
        when(serviceAvailabilityService.getServiceAvailabilityCount()).thenReturn(10L);

        mockMvc.perform(get("/api/service-availabilities/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10));

        verify(serviceAvailabilityService, times(1)).getServiceAvailabilityCount();
    }

    @Test
    void testDeleteServiceAvailability() throws Exception {
        doNothing().when(serviceAvailabilityService).deleteServiceAvailability(1L);

        mockMvc.perform(delete("/api/service-availabilities/1"))
                .andExpect(status().isNoContent());

        verify(serviceAvailabilityService, times(1)).deleteServiceAvailability(1L);
    }
}

package com.example.demo.controller;

import com.example.demo.model.ServicePlan;
import com.example.demo.service.ServicePlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(ServicePlanController.class)
@ActiveProfiles("test")
class ServicePlanControllerTest {

    @MockBean
    private ServicePlanService servicePlanService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ServicePlanController(servicePlanService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrUpdateServicePlan() throws Exception {
        ServicePlan servicePlan = new ServicePlan();
        servicePlan.setName("Basic Plan");
        servicePlan.setDescription("Basic Service Plan with limited features");

        when(servicePlanService.saveServicePlan(any(ServicePlan.class)))
                .thenReturn(servicePlan);

        mockMvc.perform(post("/api/service-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicePlan)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Basic Plan"))
                .andExpect(jsonPath("$.description").value("Basic Service Plan with limited features"));

        verify(servicePlanService, times(1)).saveServicePlan(any(ServicePlan.class));
    }

    @Test
    void testGetAllServicePlans() throws Exception {
        ServicePlan servicePlan1 = new ServicePlan();
        servicePlan1.setName("Basic Plan");
        servicePlan1.setDescription("Basic Service Plan");

        ServicePlan servicePlan2 = new ServicePlan();
        servicePlan2.setName("Premium Plan");
        servicePlan2.setDescription("Premium Service Plan with extra features");

        when(servicePlanService.getAllServicePlans()).thenReturn(Arrays.asList(servicePlan1, servicePlan2));

        mockMvc.perform(get("/api/service-plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Basic Plan"))
                .andExpect(jsonPath("$[1].name").value("Premium Plan"));

        verify(servicePlanService, times(1)).getAllServicePlans();
    }

    @Test
    void testGetServicePlanById() throws Exception {
        ServicePlan servicePlan = new ServicePlan();
        servicePlan.setName("Basic Plan");
        servicePlan.setDescription("Basic Service Plan");

        when(servicePlanService.getServicePlanById(1L)).thenReturn(Optional.of(servicePlan));

        mockMvc.perform(get("/api/service-plans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basic Plan"))
                .andExpect(jsonPath("$.description").value("Basic Service Plan"));

        verify(servicePlanService, times(1)).getServicePlanById(1L);
    }

    @Test
    void testGetServicePlanById_NotFound() throws Exception {
        when(servicePlanService.getServicePlanById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/service-plans/1"))
                .andExpect(status().isNotFound());

        verify(servicePlanService, times(1)).getServicePlanById(1L);
    }

    @Test
    void testDeleteServicePlan() throws Exception {
        doNothing().when(servicePlanService).deleteServicePlan(1L);

        mockMvc.perform(delete("/api/service-plans/1"))
                .andExpect(status().isNoContent());

        verify(servicePlanService, times(1)).deleteServicePlan(1L);
    }


    @Test
    void testAddUserToServicePlan() throws Exception {
        ServicePlan servicePlan = new ServicePlan();
        servicePlan.setName("Basic Plan");
        servicePlan.setDescription("Service Plan with users");

        when(servicePlanService.addUserToServicePlan(1L, 1L)).thenReturn(servicePlan);

        mockMvc.perform(post("/api/service-plans/1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basic Plan"));

        verify(servicePlanService, times(1)).addUserToServicePlan(1L, 1L);
    }

    @Test
    void testRemoveUserFromServicePlan() throws Exception {
        ServicePlan servicePlan = new ServicePlan();
        servicePlan.setName("Basic Plan");
        servicePlan.setDescription("Service Plan with users");

        when(servicePlanService.removeUserFromServicePlan(1L, 1L)).thenReturn(servicePlan);

        mockMvc.perform(delete("/api/service-plans/1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basic Plan"));

        verify(servicePlanService, times(1)).removeUserFromServicePlan(1L, 1L);
    }

    @Test
    void testAddServiceAvailabilityToServicePlan() throws Exception {
        ServicePlan servicePlan = new ServicePlan();
        servicePlan.setName("Basic Plan");
        servicePlan.setDescription("Service Plan with availability");

        when(servicePlanService.addServiceAvailabilityToServicePlan(1L, 1L)).thenReturn(servicePlan);

        mockMvc.perform(post("/api/service-plans/1/availabilities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basic Plan"));

        verify(servicePlanService, times(1)).addServiceAvailabilityToServicePlan(1L, 1L);
    }

    @Test
    void testRemoveServiceAvailabilityFromServicePlan() throws Exception {
        ServicePlan servicePlan = new ServicePlan();
        servicePlan.setName("Basic Plan");
        servicePlan.setDescription("Service Plan with availability");

        when(servicePlanService.removeServiceAvailabilityFromServicePlan(1L, 1L)).thenReturn(servicePlan);

        mockMvc.perform(delete("/api/service-plans/1/availabilities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Basic Plan"));

        verify(servicePlanService, times(1)).removeServiceAvailabilityFromServicePlan(1L, 1L);
    }

    @Test
    void testSearchServicePlans() throws Exception {
        ServicePlan servicePlan1 = new ServicePlan();
        servicePlan1.setName("Basic Plan");
        servicePlan1.setDescription("Basic Service Plan");

        ServicePlan servicePlan2 = new ServicePlan();
        servicePlan2.setName("Premium Plan");
        servicePlan2.setDescription("Premium Service Plan");

        when(servicePlanService.searchServicePlans("Plan")).thenReturn(Arrays.asList(servicePlan1, servicePlan2));

        mockMvc.perform(get("/api/service-plans/search?query=Plan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Basic Plan"))
                .andExpect(jsonPath("$[1].name").value("Premium Plan"));

        verify(servicePlanService, times(1)).searchServicePlans("Plan");
    }
}

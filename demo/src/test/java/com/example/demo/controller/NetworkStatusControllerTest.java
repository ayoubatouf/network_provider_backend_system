package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.NetworkStatus;
import com.example.demo.service.NetworkStatusService;
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


@WebMvcTest(NetworkStatusController.class)
@ActiveProfiles("test")
class NetworkStatusControllerTest {

    @MockBean
    private NetworkStatusService networkStatusService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new NetworkStatusController(networkStatusService)).build();
        objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    void testCreateOrUpdateNetworkStatus() throws Exception {
        NetworkStatus networkStatus = new NetworkStatus();
        networkStatus.setStatus("Active");
        networkStatus.setUpdateDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(networkStatusService.saveNetworkStatus(any(NetworkStatus.class))).thenReturn(networkStatus);

        mockMvc.perform(post("/api/network-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(networkStatus)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("Active"))
                .andExpect(jsonPath("$.updateDate").value("2023-12-17T14:30:00"));

        verify(networkStatusService, times(1)).saveNetworkStatus(any(NetworkStatus.class));
    }

    @Test
    void testUpdateNetworkStatus() throws Exception {
        NetworkStatus networkStatus = new NetworkStatus();
        networkStatus.setStatus("Updated");
        networkStatus.setUpdateDate(LocalDateTime.of(2023, 12, 18, 15, 45));

        when(networkStatusService.updateNetworkStatus(eq(1L), any(NetworkStatus.class))).thenReturn(networkStatus);

        mockMvc.perform(put("/api/network-statuses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(networkStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Updated"))
                .andExpect(jsonPath("$.updateDate").value("2023-12-18T15:45:00"));

        verify(networkStatusService, times(1)).updateNetworkStatus(eq(1L), any(NetworkStatus.class));
    }

    @Test
    void testUpdateNetworkStatus_NotFound() throws Exception {
        NetworkStatus networkStatus = new NetworkStatus();
        networkStatus.setStatus("Updated");
        networkStatus.setUpdateDate(LocalDateTime.of(2023, 12, 18, 15, 45));

        when(networkStatusService.updateNetworkStatus(eq(1L), any(NetworkStatus.class)))
                .thenThrow(new ResourceNotFoundException("NetworkStatus not found"));

        mockMvc.perform(put("/api/network-statuses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(networkStatus)))
                .andExpect(status().isNotFound());

        verify(networkStatusService, times(1)).updateNetworkStatus(eq(1L), any(NetworkStatus.class));
    }

    @Test
    void testGetAllNetworkStatuses() throws Exception {
        NetworkStatus networkStatus1 = new NetworkStatus();
        networkStatus1.setStatus("Active");
        networkStatus1.setUpdateDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        NetworkStatus networkStatus2 = new NetworkStatus();
        networkStatus2.setStatus("Inactive");
        networkStatus2.setUpdateDate(LocalDateTime.of(2023, 12, 16, 10, 15));

        when(networkStatusService.getAllNetworkStatuses()).thenReturn(Arrays.asList(networkStatus1, networkStatus2));

        mockMvc.perform(get("/api/network-statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Active"))
                .andExpect(jsonPath("$[1].status").value("Inactive"));

        verify(networkStatusService, times(1)).getAllNetworkStatuses();
    }

    @Test
    void testGetNetworkStatusById() throws Exception {
        NetworkStatus networkStatus = new NetworkStatus();
        networkStatus.setStatus("Active");
        networkStatus.setUpdateDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(networkStatusService.getNetworkStatusById(1L)).thenReturn(Optional.of(networkStatus));

        mockMvc.perform(get("/api/network-statuses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Active"))
                .andExpect(jsonPath("$.updateDate").value("2023-12-17T14:30:00"));

        verify(networkStatusService, times(1)).getNetworkStatusById(1L);
    }

    @Test
    void testGetNetworkStatusById_NotFound() throws Exception {
        when(networkStatusService.getNetworkStatusById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/network-statuses/1"))
                .andExpect(status().isNotFound());

        verify(networkStatusService, times(1)).getNetworkStatusById(1L);
    }

    @Test
    void testGetNetworkStatusesByRegion() throws Exception {
        NetworkStatus networkStatus1 = new NetworkStatus();
        networkStatus1.setStatus("Active");
        networkStatus1.setUpdateDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(networkStatusService.getNetworkStatusesByRegion(1L)).thenReturn(Arrays.asList(networkStatus1));

        mockMvc.perform(get("/api/network-statuses/region/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Active"));

        verify(networkStatusService, times(1)).getNetworkStatusesByRegion(1L);
    }

    @Test
    void testGetNetworkStatusesByDateRange() throws Exception {
        NetworkStatus networkStatus1 = new NetworkStatus();
        networkStatus1.setStatus("Active");
        networkStatus1.setUpdateDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(networkStatusService.getNetworkStatusesByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(networkStatus1));

        mockMvc.perform(get("/api/network-statuses/date-range")
                        .param("startDate", "2023-12-16T00:00:00")
                        .param("endDate", "2023-12-18T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Active"));

        verify(networkStatusService, times(1)).getNetworkStatusesByDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void testGetNetworkStatusesByServiceAvailability() throws Exception {
        NetworkStatus networkStatus1 = new NetworkStatus();
        networkStatus1.setStatus("Active");
        networkStatus1.setUpdateDate(LocalDateTime.of(2023, 12, 17, 14, 30));

        when(networkStatusService.getNetworkStatusesByServiceAvailability(1L)).thenReturn(Arrays.asList(networkStatus1));

        mockMvc.perform(get("/api/network-statuses/service-availability/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Active"));

        verify(networkStatusService, times(1)).getNetworkStatusesByServiceAvailability(1L);
    }

    @Test
    void testBulkUpdateNetworkStatuses() throws Exception {
        doNothing().when(networkStatusService).bulkUpdateNetworkStatuses(anyList(), anyString());

        mockMvc.perform(put("/api/network-statuses/bulk-update")
                        .param("ids", "1", "2")
                        .param("newStatus", "Inactive"))
                .andExpect(status().isOk());

        verify(networkStatusService, times(1)).bulkUpdateNetworkStatuses(anyList(), anyString());
    }

    @Test
    void testDeleteNetworkStatus() throws Exception {
        doNothing().when(networkStatusService).deleteNetworkStatus(1L);

        mockMvc.perform(delete("/api/network-statuses/1"))
                .andExpect(status().isNoContent());

        verify(networkStatusService, times(1)).deleteNetworkStatus(1L);
    }
}

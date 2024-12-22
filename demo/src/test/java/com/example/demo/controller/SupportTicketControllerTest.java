package com.example.demo.controller;

import com.example.demo.model.SupportTicket;
import com.example.demo.service.SupportTicketService;
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

@WebMvcTest(SupportTicketController.class)
@ActiveProfiles("test")
class SupportTicketControllerTest {

    @MockBean
    private SupportTicketService supportTicketService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SupportTicketController(supportTicketService)).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    void testCreateOrUpdateSupportTicket() throws Exception {
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setIssueDescription("Issue with service");
        supportTicket.setStatus("Open");
        supportTicket.setCreatedDate(LocalDateTime.of(2023, 12, 17, 10, 0));

        when(supportTicketService.saveSupportTicket(any(SupportTicket.class)))
                .thenReturn(supportTicket);

        mockMvc.perform(post("/api/support-tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supportTicket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.issueDescription").value("Issue with service"))
                .andExpect(jsonPath("$.status").value("Open"))
                .andExpect(jsonPath("$.createdDate").value("2023-12-17T10:00:00"));

        verify(supportTicketService, times(1)).saveSupportTicket(any(SupportTicket.class));
    }

    @Test
    void testGetAllSupportTickets() throws Exception {
        SupportTicket ticket1 = new SupportTicket();
        ticket1.setIssueDescription("Issue with login");
        ticket1.setStatus("Resolved");
        ticket1.setCreatedDate(LocalDateTime.of(2023, 12, 15, 9, 30));

        SupportTicket ticket2 = new SupportTicket();
        ticket2.setIssueDescription("Network connectivity issue");
        ticket2.setStatus("Open");
        ticket2.setCreatedDate(LocalDateTime.of(2023, 12, 16, 14, 45));

        when(supportTicketService.getAllSupportTickets()).thenReturn(Arrays.asList(ticket1, ticket2));

        mockMvc.perform(get("/api/support-tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueDescription").value("Issue with login"))
                .andExpect(jsonPath("$[1].issueDescription").value("Network connectivity issue"))
                .andExpect(jsonPath("$[0].createdDate").value("2023-12-15T09:30:00"))
                .andExpect(jsonPath("$[1].createdDate").value("2023-12-16T14:45:00"));

        verify(supportTicketService, times(1)).getAllSupportTickets();
    }

    @Test
    void testGetSupportTicketById() throws Exception {
        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setIssueDescription("Issue with service");
        supportTicket.setStatus("Open");
        supportTicket.setCreatedDate(LocalDateTime.of(2023, 12, 17, 10, 0));

        when(supportTicketService.getSupportTicketById(1L)).thenReturn(Optional.of(supportTicket));

        mockMvc.perform(get("/api/support-tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issueDescription").value("Issue with service"))
                .andExpect(jsonPath("$.status").value("Open"))
                .andExpect(jsonPath("$.createdDate").value("2023-12-17T10:00:00"));

        verify(supportTicketService, times(1)).getSupportTicketById(1L);
    }

    @Test
    void testGetSupportTicketById_NotFound() throws Exception {
        when(supportTicketService.getSupportTicketById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/support-tickets/1"))
                .andExpect(status().isNotFound());

        verify(supportTicketService, times(1)).getSupportTicketById(1L);
    }

    @Test
    void testDeleteSupportTicket() throws Exception {
        doNothing().when(supportTicketService).deleteSupportTicket(1L);

        mockMvc.perform(delete("/api/support-tickets/1"))
                .andExpect(status().isNoContent());

        verify(supportTicketService, times(1)).deleteSupportTicket(1L);
    }

    @Test
    void testGetSupportTicketsByStatus() throws Exception {
        SupportTicket ticket = new SupportTicket();
        ticket.setIssueDescription("Issue with login");
        ticket.setStatus("Open");
        ticket.setCreatedDate(LocalDateTime.of(2023, 12, 15, 9, 30));

        when(supportTicketService.getSupportTicketsByStatus("Open")).thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/support-tickets/status/Open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueDescription").value("Issue with login"))
                .andExpect(jsonPath("$[0].status").value("Open"));

        verify(supportTicketService, times(1)).getSupportTicketsByStatus("Open");
    }


    @Test
    void testCountSupportTicketsByStatus() throws Exception {
        when(supportTicketService.countSupportTicketsByStatus("Open")).thenReturn(5L);

        mockMvc.perform(get("/api/support-tickets/count/status/Open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));

        verify(supportTicketService, times(1)).countSupportTicketsByStatus("Open");
    }

    @Test
    void testGetSupportTicketsByCreatedDateRange() throws Exception {
        SupportTicket ticket = new SupportTicket();
        ticket.setIssueDescription("Issue with login");
        ticket.setStatus("Open");
        ticket.setCreatedDate(LocalDateTime.of(2023, 12, 15, 9, 30));

        when(supportTicketService.getSupportTicketsByCreatedDateRange(any(), any()))
                .thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/support-tickets/created-between")
                        .param("startDate", "2023-12-15T00:00:00")
                        .param("endDate", "2023-12-16T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueDescription").value("Issue with login"));

        verify(supportTicketService, times(1)).getSupportTicketsByCreatedDateRange(any(), any());
    }

    @Test
    void testSearchSupportTicketsByIssueDescription() throws Exception {
        SupportTicket ticket = new SupportTicket();
        ticket.setIssueDescription("Issue with login");
        ticket.setStatus("Open");
        ticket.setCreatedDate(LocalDateTime.of(2023, 12, 15, 9, 30));

        when(supportTicketService.searchSupportTicketsByIssueDescription("login")).thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/support-tickets/search")
                        .param("keyword", "login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].issueDescription").value("Issue with login"));

        verify(supportTicketService, times(1)).searchSupportTicketsByIssueDescription("login");
    }
}

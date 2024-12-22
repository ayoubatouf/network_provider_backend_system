package com.example.demo.controller;

import com.example.demo.model.ServicePlan;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("johnatan");
        user.setEmail("johnatan33@example.com");

        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("johnatan"))
                .andExpect(jsonPath("$.email").value("johnatan33@example.com"));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("johnatan");
        user1.setEmail("johnatan33@example.com");

        User user2 = new User();
        user2.setUsername("jack");
        user2.setEmail("jack33@example.com");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("johnatan"))
                .andExpect(jsonPath("$[1].username").value("jack"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setUsername("johnatan");
        user.setEmail("johnatan33@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johnatan"))
                .andExpect(jsonPath("$.email").value("johnatan33@example.com"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("updatedUser");
        user.setEmail("updated@example.com");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testCheckUsernameExists() throws Exception {
        when(userService.existsByUsername("johnatan")).thenReturn(true);

        mockMvc.perform(get("/api/users/exists/username/johnatan"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).existsByUsername("johnatan");
    }

    @Test
    void testCheckEmailExists() throws Exception {
        when(userService.existsByEmail("johnatan33@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/users/exists/email/johnatan33@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).existsByEmail("johnatan33@example.com");
    }

    @Test
    void testSearchUsersByUsername() throws Exception {
        User user = new User();
        user.setUsername("johnatan");
        user.setEmail("johnatan33@example.com");

        when(userService.searchUsersByUsername("john")).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/api/users/search/username")
                        .param("username", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("johnatan"));

        verify(userService, times(1)).searchUsersByUsername("john");
    }

    @Test
    void testAssignServicePlan() throws Exception {
        User user = new User();
        user.setUsername("johnatan");

        ServicePlan servicePlan = new ServicePlan(); 
        servicePlan.setName("Premium");

        when(userService.assignServicePlan(eq(1L), any(ServicePlan.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/1/service-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicePlan)))
                .andExpect(status().isOk());

        verify(userService, times(1)).assignServicePlan(eq(1L), any(ServicePlan.class));
    }

    @Test
    void testRemoveServicePlan() throws Exception {
        User user = new User();
        user.setUsername("johnatan");

        ServicePlan servicePlan = new ServicePlan(); 
        servicePlan.setName("Premium");

        when(userService.removeServicePlan(eq(1L), any(ServicePlan.class))).thenReturn(user);

        mockMvc.perform(delete("/api/users/1/service-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicePlan)))
                .andExpect(status().isOk());

        verify(userService, times(1)).removeServicePlan(eq(1L), any(ServicePlan.class));
    }
}

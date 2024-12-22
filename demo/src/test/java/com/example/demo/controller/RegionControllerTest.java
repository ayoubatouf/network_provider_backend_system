package com.example.demo.controller;

import com.example.demo.model.Region;
import com.example.demo.service.RegionService;
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

@WebMvcTest(RegionController.class)
@ActiveProfiles("test")
class RegionControllerTest {

    @MockBean
    private RegionService regionService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RegionController(regionService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrUpdateRegion() throws Exception {
        Region region = new Region();
        region.setName("North America");
        region.setDescription("Region including USA, Canada, Mexico");

        when(regionService.saveRegion(any(Region.class))).thenReturn(region);

        mockMvc.perform(post("/api/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(region)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("North America"))
                .andExpect(jsonPath("$.description").value("Region including USA, Canada, Mexico"));

        verify(regionService, times(1)).saveRegion(any(Region.class));
    }

    @Test
    void testGetAllRegions() throws Exception {
        Region region1 = new Region();
        region1.setName("North America");
        region1.setDescription("Region including USA, Canada, Mexico");

        Region region2 = new Region();
        region2.setName("Europe");
        region2.setDescription("Region including UK, Germany, France");

        when(regionService.getAllRegions()).thenReturn(Arrays.asList(region1, region2));

        mockMvc.perform(get("/api/regions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("North America"))
                .andExpect(jsonPath("$[1].name").value("Europe"));

        verify(regionService, times(1)).getAllRegions();
    }

    @Test
    void testGetRegionById() throws Exception {
        Region region = new Region();
        region.setName("North America");
        region.setDescription("Region including USA, Canada, Mexico");

        when(regionService.getRegionById(1L)).thenReturn(Optional.of(region));

        mockMvc.perform(get("/api/regions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("North America"))
                .andExpect(jsonPath("$.description").value("Region including USA, Canada, Mexico"));

        verify(regionService, times(1)).getRegionById(1L);
    }

    @Test
    void testGetRegionById_NotFound() throws Exception {
        when(regionService.getRegionById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/regions/1"))
                .andExpect(status().isNotFound());

        verify(regionService, times(1)).getRegionById(1L);
    }

    @Test
    void testDeleteRegion() throws Exception {
        doNothing().when(regionService).deleteRegion(1L);

        mockMvc.perform(delete("/api/regions/1"))
                .andExpect(status().isNoContent());

        verify(regionService, times(1)).deleteRegion(1L);
    }

    @Test
    void testUpdateRegion() throws Exception {
        Region region = new Region();
        region.setName("North America");
        region.setDescription("Updated Region including USA, Canada, Mexico");

        when(regionService.updateRegion(eq(1L), any(Region.class))).thenReturn(region);

        mockMvc.perform(put("/api/regions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(region)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("North America"))
                .andExpect(jsonPath("$.description").value("Updated Region including USA, Canada, Mexico"));

        verify(regionService, times(1)).updateRegion(eq(1L), any(Region.class));
    }

  
    @Test
    void testRemoveUserFromRegion() throws Exception {
        Region region = new Region();
        region.setName("North America");
        region.setDescription("Region including USA, Canada, Mexico");

        when(regionService.removeUserFromRegion(eq(1L), eq(1L))).thenReturn(region);

        mockMvc.perform(delete("/api/regions/1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("North America"))
                .andExpect(jsonPath("$.users.size()").value(0));

        verify(regionService, times(1)).removeUserFromRegion(eq(1L), eq(1L));
    }

    @Test
    void testRemoveNetworkStatusFromRegion() throws Exception {
        Region region = new Region();
        region.setName("North America");
        region.setDescription("Region including USA, Canada, Mexico");

        when(regionService.removeNetworkStatusFromRegion(eq(1L), eq(1L))).thenReturn(region);

        mockMvc.perform(delete("/api/regions/1/network-statuses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("North America"))
                .andExpect(jsonPath("$.networkStatuses.size()").value(0));

        verify(regionService, times(1)).removeNetworkStatusFromRegion(eq(1L), eq(1L));
    }

    @Test
    void testSearchRegionsByName() throws Exception {
        Region region1 = new Region();
        region1.setName("North America");
        region1.setDescription("Region including USA, Canada, Mexico");

        Region region2 = new Region();
        region2.setName("South America");
        region2.setDescription("Region including Brazil, Argentina, Chile");

        when(regionService.searchRegionsByName("America")).thenReturn(Arrays.asList(region1, region2));

        mockMvc.perform(get("/api/regions/search")
                        .param("name", "America"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("North America"))
                .andExpect(jsonPath("$[1].name").value("South America"));

        verify(regionService, times(1)).searchRegionsByName("America");
    }

    @Test
    void testFilterRegionsByDescription() throws Exception {
        Region region1 = new Region();
        region1.setName("North America");
        region1.setDescription("Region including USA, Canada, Mexico");

        Region region2 = new Region();
        region2.setName("Europe");
        region2.setDescription("Region including UK, Germany, France");

        when(regionService.filterRegionsByDescription("USA")).thenReturn(Arrays.asList(region1));

        mockMvc.perform(get("/api/regions/filter")
                        .param("description", "USA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("North America"));

        verify(regionService, times(1)).filterRegionsByDescription("USA");
    }
}

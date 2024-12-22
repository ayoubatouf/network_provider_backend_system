package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.NetworkStatus;
import com.example.demo.model.Region;
import com.example.demo.model.User;
import com.example.demo.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    public ResponseEntity<Region> createOrUpdateRegion(@RequestBody Region region) {
        Region savedRegion = regionService.saveRegion(region);
        return new ResponseEntity<>(savedRegion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Region>> getAllRegions() {
        List<Region> regions = regionService.getAllRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        Optional<Region> region = regionService.getRegionById(id);
        return region.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(@PathVariable Long id, @RequestBody Region updatedRegion) {
        try {
            Region region = regionService.updateRegion(id, updatedRegion);
            return new ResponseEntity<>(region, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{regionId}/users")
    public ResponseEntity<Region> addUserToRegion(@PathVariable Long regionId, @RequestBody User user) {
        Region updatedRegion = regionService.addUserToRegion(regionId, user);
        return new ResponseEntity<>(updatedRegion, HttpStatus.OK);
    }

    @DeleteMapping("/{regionId}/users/{userId}")
    public ResponseEntity<Region> removeUserFromRegion(@PathVariable Long regionId, @PathVariable Long userId) {
        try {
            Region updatedRegion = regionService.removeUserFromRegion(regionId, userId);
            return new ResponseEntity<>(updatedRegion, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{regionId}/network-statuses")
    public ResponseEntity<Region> addNetworkStatusToRegion(@PathVariable Long regionId, @RequestBody NetworkStatus networkStatus) {
        Region updatedRegion = regionService.addNetworkStatusToRegion(regionId, networkStatus);
        return new ResponseEntity<>(updatedRegion, HttpStatus.OK);
    }

    @DeleteMapping("/{regionId}/network-statuses/{networkStatusId}")
    public ResponseEntity<Region> removeNetworkStatusFromRegion(@PathVariable Long regionId, @PathVariable Long networkStatusId) {
        try {
            Region updatedRegion = regionService.removeNetworkStatusFromRegion(regionId, networkStatusId);
            return new ResponseEntity<>(updatedRegion, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Region>> searchRegionsByName(@RequestParam String name) {
        List<Region> regions = regionService.searchRegionsByName(name);
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Region>> filterRegionsByDescription(@RequestParam String description) {
        List<Region> regions = regionService.filterRegionsByDescription(description);
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }
}

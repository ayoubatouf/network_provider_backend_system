package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.NetworkStatus;
import com.example.demo.model.Region;
import com.example.demo.model.User;
import com.example.demo.repository.RegionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    @Autowired
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    public Region saveRegion(Region region) {
        return regionRepository.save(region);
    }

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public Optional<Region> getRegionById(Long id) {
        return regionRepository.findById(id);
    }

    public void deleteRegion(Long id) {
        if (regionRepository.existsById(id)) {
            regionRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Region with id " + id + " not found");
        }
    }

    public Region updateRegion(Long id, Region updatedRegion) {
        return regionRepository.findById(id)
                .map(region -> {
                    region.setName(updatedRegion.getName());
                    region.setDescription(updatedRegion.getDescription());
                    return regionRepository.save(region);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Region with id " + id + " not found"));
    }

    public Region addUserToRegion(Long regionId, User user) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region with id " + regionId + " not found"));

        region.getUsers().add(user);
        user.setRegion(region); 
        return regionRepository.save(region);
    }

    public Region removeUserFromRegion(Long regionId, Long userId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region with id " + regionId + " not found"));

        User userToRemove = region.getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found in region"));

        region.getUsers().remove(userToRemove);
        userToRemove.setRegion(null); 
        return regionRepository.save(region);
    }

    public Region addNetworkStatusToRegion(Long regionId, NetworkStatus networkStatus) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region with id " + regionId + " not found"));

        region.getNetworkStatuses().add(networkStatus);
        networkStatus.setRegion(region); 
        return regionRepository.save(region);
    }

    public Region removeNetworkStatusFromRegion(Long regionId, Long networkStatusId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new ResourceNotFoundException("Region with id " + regionId + " not found"));

        NetworkStatus statusToRemove = region.getNetworkStatuses().stream()
                .filter(status -> status.getId().equals(networkStatusId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Network status with id " + networkStatusId + " not found"));

        region.getNetworkStatuses().remove(statusToRemove);
        statusToRemove.setRegion(null); 
        return regionRepository.save(region);
    }

    public List<Region> searchRegionsByName(String name) {
        return regionRepository.findByNameIgnoreCaseContaining(name);
    }

    public List<Region> filterRegionsByDescription(String description) {
        return regionRepository.findByDescriptionIgnoreCaseContaining(description);
    }


}


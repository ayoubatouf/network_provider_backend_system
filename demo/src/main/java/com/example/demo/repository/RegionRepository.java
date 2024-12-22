package com.example.demo.repository;

import com.example.demo.model.Region;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByDescriptionIgnoreCaseContaining(String description);
    List<Region> findByNameIgnoreCaseContaining(String name);
   
}

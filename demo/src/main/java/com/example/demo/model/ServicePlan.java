package com.example.demo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_plans")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ServicePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @NotNull
    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @ManyToMany(mappedBy = "servicePlans")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "servicePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "servicePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "service_plan_availability",
        joinColumns = @JoinColumn(name = "service_plan_id"),
        inverseJoinColumns = @JoinColumn(name = "service_availability_id")
    )
    private Set<ServiceAvailability> serviceAvailabilities = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}

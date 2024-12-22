package com.example.demo.model;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "support_tickets")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5, max = 1000)
    @Column(nullable = false, length = 1000)
    private String issueDescription;

    @NotNull
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String status;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime auditingCreatedDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}

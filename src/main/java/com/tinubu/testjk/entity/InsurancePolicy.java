package com.tinubu.testjk.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tinubu.testjk.enums.PolicyStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class InsurancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name should not be blank")
    private String name;

    @NotNull(message = "Status should not be blank")
    @Enumerated(EnumType.STRING)
    private PolicyStatus status;

    @NotNull(message = "Coverage start date shoud not be null")
    private LocalDate coverageStartDate;

    @NotNull(message = "Coverage end date shoud not be null")
    private LocalDate coverageEndDate;

    private LocalDateTime creationDate;

    private LocalDateTime lastUpdateDate;

    @PrePersist
    protected void onCreate() {
       this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void updateEndDate() {
        this.lastUpdateDate = LocalDateTime.now();
    }

}

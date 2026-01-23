package com.epam.finaltask.model.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class AuditableEntity {

    @Id
    private UUID id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

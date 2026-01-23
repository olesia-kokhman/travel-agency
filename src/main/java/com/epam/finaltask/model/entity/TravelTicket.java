package com.epam.finaltask.model.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class TravelTicket extends AuditableEntity {

    private String ticketNumber;
    private Order order;
    private LocalDateTime issuedAt;

}

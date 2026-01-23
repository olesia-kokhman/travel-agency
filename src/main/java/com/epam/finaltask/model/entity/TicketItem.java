package com.epam.finaltask.model.entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class TicketItem extends AuditableEntity {

    private TravelTicket travelTicket;
    private String ticketItemNumber;
    private LocalDateTime issuedAt;


}

package com.epam.finaltask.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_extras", uniqueConstraints = @UniqueConstraint(name = "uk_ticket_items_item_number", columnNames = "item_number"))
public class TicketExtra extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "item_number", nullable = false, updatable = false)
    private String itemNumber;

    @Column(name = "issued_at", nullable = false, updatable = false)
    private LocalDateTime issuedAt;

}

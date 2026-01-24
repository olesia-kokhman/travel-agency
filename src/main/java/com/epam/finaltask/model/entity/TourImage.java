package com.epam.finaltask.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class TourImage extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "is_main")
    private boolean main;

    @Column(name = "alt")
    private String alt;
}

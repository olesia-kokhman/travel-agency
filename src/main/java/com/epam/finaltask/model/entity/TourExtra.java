package com.epam.finaltask.model.entity;

import com.epam.finaltask.model.enums.ExtraServiceType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tour_extras")
public class TourExtra extends AuditableEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Lob
    @Column(name = "long_description", nullable = false)
    private String longDescription;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExtraServiceType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

}

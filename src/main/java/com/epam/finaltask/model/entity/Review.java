package com.epam.finaltask.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Review extends AuditableEntity {

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

}

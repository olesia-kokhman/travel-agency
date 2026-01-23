package com.epam.finaltask.model.entity;

import jakarta.persistence.Entity;

@Entity
public class Review extends AuditableEntity {

    private String comment;
    private Integer rating;
    private Order order;

}

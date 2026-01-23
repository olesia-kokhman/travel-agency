package com.epam.finaltask.model.entity;

import com.epam.finaltask.model.enums.OrderStatus;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Order extends AuditableEntity {

    private Integer orderNumber;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private User user;

    private Payment payment;
    private Review review;

    private Tour tour;
    private Set<ExtraService> chosenExtraServices;

}

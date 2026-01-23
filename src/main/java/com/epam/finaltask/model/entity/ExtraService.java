package com.epam.finaltask.model.entity;

import com.epam.finaltask.model.enums.ExtraServiceType;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class ExtraService extends AuditableEntity {

    private String name;
    private String shortDescription;
    private String longDescription;
    private BigDecimal price;
    private boolean isActive;
    private Integer capacity;

    private ExtraServiceType extraServiceType;

}

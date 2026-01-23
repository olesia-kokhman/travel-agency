package com.epam.finaltask.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import jakarta.persistence.*;

@Entity
public class Tour extends AuditableEntity {

    private String title;
    private String longDescription;
    private String shortDescription;
    private BigDecimal price;
    private String country;
    private String city;

    private boolean isHot;
    private boolean isActive;
    private Integer capacity;

    private TourType tourType;
    private TransferType transferType;
    private HotelType hotelType;

    //private List<String> imageUrls;
    private Set<ExtraService> extraServices;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

}

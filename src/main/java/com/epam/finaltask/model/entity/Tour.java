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
@Table(name = "tours")
public class Tour extends AuditableEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "long_description", nullable = false)
    private String longDescription;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "is_hot", nullable = false)
    private boolean hot;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "capacity")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "tour_type", nullable = false)
    private TourType tourType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", nullable = false)
    private TransferType transferType;

    @Enumerated(EnumType.STRING)
    @Column(name = "hotel_type", nullable = false)
    private HotelType hotelType;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TourImage> images;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TourExtra> extras;

    @Column(name = "check_in_datetime", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out_datetime", nullable = false)
    private LocalDateTime checkOut;

}

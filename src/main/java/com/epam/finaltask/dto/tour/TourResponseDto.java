package com.epam.finaltask.dto.tour;

import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourResponseDto {

    private UUID id;
    private String title;
    private String longDescription;
    private String shortDescription;

    private BigDecimal price;
    private String country;
    private String city;

    private boolean hot;
    private boolean active;
    private Integer capacity;

    private TourType tourType;
    private TransferType transferType;
    private HotelType hotelType;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

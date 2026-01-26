package com.epam.finaltask.dto.tour.extra;

import com.epam.finaltask.model.enums.ExtraServiceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TourExtraRequestDto {

    private String title;
    private String shortDescription;
    private String longDescription;

    private BigDecimal price;

    private boolean active;
    private Integer capacity;

    private ExtraServiceType type;
}

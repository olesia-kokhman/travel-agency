package com.epam.finaltask.dto.tour.image;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TourImageResponseDto {

    private UUID id;

    private String url;
    private boolean main;
    private String alt;

    private UUID tourId;
}

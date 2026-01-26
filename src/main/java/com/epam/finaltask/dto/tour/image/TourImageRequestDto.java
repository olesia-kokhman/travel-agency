package com.epam.finaltask.dto.tour.image;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TourImageRequestDto {

    private String url;
    private boolean main;
    private String alt;
}

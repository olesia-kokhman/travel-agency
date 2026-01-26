package com.epam.finaltask.dto.order.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDto {

    private String comment;
    private Integer rating;
}

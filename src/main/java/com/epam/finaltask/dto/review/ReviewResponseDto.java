package com.epam.finaltask.dto.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {

    private UUID id;

    private String comment;
    private Integer rating;

    private UUID orderId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

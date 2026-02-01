package com.epam.finaltask.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {

    @NotNull(message = "Tour id is required")
    private UUID tourId;


    //private Set<UUID> tourExtraIds;
}

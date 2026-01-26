package com.epam.finaltask.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

    private UUID tourId;
    private Set<UUID> tourExtraIds;
}

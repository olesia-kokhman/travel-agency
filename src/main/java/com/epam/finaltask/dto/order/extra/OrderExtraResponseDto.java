package com.epam.finaltask.dto.order.extra;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderExtraResponseDto {

    private UUID id;

    private String extraNumber;
    private BigDecimal amount;

    private UUID orderId;
}

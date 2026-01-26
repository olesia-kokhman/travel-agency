package com.epam.finaltask.dto.order.extra;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderExtraRequestDto {

    private String extraNumber;
    private BigDecimal amount;
}

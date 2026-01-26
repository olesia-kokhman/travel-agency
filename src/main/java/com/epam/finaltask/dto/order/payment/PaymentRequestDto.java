package com.epam.finaltask.dto.order.payment;

import com.epam.finaltask.model.enums.PaymentMethod;
import com.epam.finaltask.model.enums.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDto {

    private PaymentMethod paymentMethod;
    private PaymentStatus status;

    private BigDecimal amount;
    private String failureReason;
}

package com.epam.finaltask.dto.payment;

import com.epam.finaltask.model.enums.PaymentMethod;
import com.epam.finaltask.model.enums.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponseDto {

    private UUID id;

    private PaymentMethod paymentMethod;
    private PaymentStatus status;

    private LocalDateTime paidAt;
    private BigDecimal amount;

    private String failureReason;
    private UUID orderId;
}

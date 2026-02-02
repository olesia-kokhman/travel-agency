package com.epam.finaltask.dto.payment;

import com.epam.finaltask.model.enums.PaymentMethod;
import com.epam.finaltask.model.enums.PaymentStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDto {

    @NotNull(message = "paymentMethod is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "amount must be greater than 0")
    @DecimalMax(value = "1000000000.00", inclusive = true, message = "amount is too large")
    @Digits(integer = 19, fraction = 2, message = "amount must have up to 19 digits and 2 decimal places")
    private BigDecimal amount;

}

package com.epam.finaltask.repository.specifications.filters;

import com.epam.finaltask.model.enums.PaymentMethod;
import com.epam.finaltask.model.enums.PaymentStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentFilter {

    private List<PaymentStatus> statuses;
    private List<PaymentMethod> methods;

    @DecimalMin(value = "0.00", inclusive = true, message = "minAmount must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "minAmount must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal minAmount;

    @DecimalMin(value = "0.00", inclusive = true, message = "maxAmount must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "maxAmount must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal maxAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime paidFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime paidTo;

    private Boolean hasFailureReason;

    @AssertTrue(message = "minAmount must be <= maxAmount")
    public boolean isAmountRangeValid() {
        if (minAmount == null || maxAmount == null) return true;
        return minAmount.compareTo(maxAmount) <= 0;
    }

    @AssertTrue(message = "paidFrom must be <= paidTo")
    public boolean isPaidRangeValid() {
        if (paidFrom == null || paidTo == null) return true;
        return !paidFrom.isAfter(paidTo);
    }
}

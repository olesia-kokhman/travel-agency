package com.epam.finaltask.repository.specifications.filters;

import com.epam.finaltask.model.enums.OrderStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderFilter {

    private List<OrderStatus> statuses;

    @DecimalMin(value = "0.00", inclusive = true, message = "minTotalAmount must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "minTotalAmount must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal minTotalAmount;

    @DecimalMin(value = "0.00", inclusive = true, message = "maxTotalAmount must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "maxTotalAmount must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal maxTotalAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdTo;

    private Boolean hasPayment;
    private Boolean hasReview;

    @AssertTrue(message = "minTotalAmount must be <= maxTotalAmount")
    public boolean isTotalAmountRangeValid() {
        if (minTotalAmount == null || maxTotalAmount == null) return true;
        return minTotalAmount.compareTo(maxTotalAmount) <= 0;
    }

    @AssertTrue(message = "createdFrom must be <= createdTo")
    public boolean isCreatedAtRangeValid() {
        if (createdFrom == null || createdTo == null) return true;
        return !createdFrom.isAfter(createdTo);
    }
}

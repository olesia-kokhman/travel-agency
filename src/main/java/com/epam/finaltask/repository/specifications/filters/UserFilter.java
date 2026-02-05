package com.epam.finaltask.repository.specifications.filters;

import com.epam.finaltask.model.enums.UserRole;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserFilter {
    
    @Size(max = 120, message = "q must be at most 120 characters")
    private String q;

    private List<UserRole> roles;

    private Boolean active;

    @DecimalMin(value = "0.00", inclusive = true, message = "minBalance must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "minBalance must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal minBalance;

    @DecimalMin(value = "0.00", inclusive = true, message = "maxBalance must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "maxBalance must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal maxBalance;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdTo;

    @AssertTrue(message = "minBalance must be <= maxBalance")
    public boolean isBalanceRangeValid() {
        if (minBalance == null || maxBalance == null) return true;
        return minBalance.compareTo(maxBalance) <= 0;
    }

    @AssertTrue(message = "createdFrom must be <= createdTo")
    public boolean isCreatedAtRangeValid() {
        if (createdFrom == null || createdTo == null) return true;
        return !createdFrom.isAfter(createdTo);
    }
}

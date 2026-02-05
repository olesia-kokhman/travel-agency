package com.epam.finaltask.repository.specifications.filters;

import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TourFilter {

    @Size(max = 120, message = "q must be at most 120 characters")
    private String q;

    private List<TourType> types;
    private List<TransferType> transferTypes;
    private List<HotelType> hotelTypes;

    @DecimalMin(value = "0.00", inclusive = true, message = "minPrice must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "minPrice must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.00", inclusive = true, message = "maxPrice must be >= 0")
    @Digits(integer = 17, fraction = 2, message = "maxPrice must have up to 17 integer digits and 2 fraction digits")
    private BigDecimal maxPrice;

    @Size(max = 50, message = "country must be at most 50 characters")
    private String country;

    @Size(max = 50, message = "city must be at most 50 characters")
    private String city;

    private Boolean hot;
    private Boolean active;

    @Min(value = 0, message = "minCapacity must be >= 0")
    private Integer minCapacity;

    @Min(value = 0, message = "maxCapacity must be >= 0")
    private Integer maxCapacity;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkInFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkInTo;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkOutFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkOutTo;

    @AssertTrue(message = "minPrice must be <= maxPrice")
    public boolean isPriceRangeValid() {
        if (minPrice == null || maxPrice == null) return true;
        return minPrice.compareTo(maxPrice) <= 0;
    }

    @AssertTrue(message = "minCapacity must be <= maxCapacity")
    public boolean isCapacityRangeValid() {
        if (minCapacity == null || maxCapacity == null) return true;
        return minCapacity <= maxCapacity;
    }

    @AssertTrue(message = "checkInFrom must be <= checkInTo")
    public boolean isCheckInRangeValid() {
        if (checkInFrom == null || checkInTo == null) return true;
        return !checkInFrom.isAfter(checkInTo);
    }

    @AssertTrue(message = "checkOutFrom must be <= checkOutTo")
    public boolean isCheckOutRangeValid() {
        if (checkOutFrom == null || checkOutTo == null) return true;
        return !checkOutFrom.isAfter(checkOutTo);
    }
}

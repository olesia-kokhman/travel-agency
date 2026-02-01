package com.epam.finaltask.dto.tour;

import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourCreateDto {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters long")
    private String title;

    @NotBlank(message = "Long description is required")
    @Size(max = 5000, message = "Long description must be at most 5000 characters long")
    private String longDescription;

    @NotBlank(message = "Short description is required")
    @Size(max = 500, message = "Short description must be at most 500 characters long")
    private String shortDescription;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Price must have up to 17 digits and 2 decimals")
    private BigDecimal price;

    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country must be at most 50 characters long")
    private String country;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must be at most 50 characters long")
    private String city;

    @NotNull(message = "Hot flag is required")
    private Boolean hot;

    @NotNull(message = "Active flag is required")
    private Boolean active;

    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @NotNull(message = "Tour type is required")
    private TourType tourType;

    @NotNull(message = "Transfer type is required")
    private TransferType transferType;

    @NotNull(message = "Hotel type is required")
    private HotelType hotelType;

    @NotNull(message = "Check-in datetime is required")
    @FutureOrPresent
    private LocalDateTime checkIn;

    @NotNull(message = "Check-out datetime is required")
    @FutureOrPresent
    private LocalDateTime checkOut;

    @AssertTrue(message = "Check-out datetime must be after check-in datetime")
    public boolean isDateRangeValid() {
        if (checkIn == null || checkOut == null) return true;
        return checkOut.isAfter(checkIn);
    }
}

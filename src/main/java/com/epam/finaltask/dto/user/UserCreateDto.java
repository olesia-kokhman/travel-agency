package com.epam.finaltask.dto.user;

import com.epam.finaltask.model.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters long")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(max = 100, message = "Surname must be at most 100 characters long")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 255, message = "Email must be at most 255 characters long")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 25, message = "Phone number must be at most 25 characters long")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must contain 10-15 digits and may start with +"
    )
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be 8-72 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, and 1 digit"
    )
    private String password;

    private boolean active;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Balance must have up to 17 digits and 2 decimals")
    private BigDecimal balance;

    @NotNull(message = "Role is required")
    private UserRole role;
}

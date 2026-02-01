package com.epam.finaltask.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateProfileDto {

    @Size(max = 100, message = "Name must be at most 100 characters long")
    private String name;

    @Size(max = 100, message = "Surname must be at most 100 characters long")
    private String surname;

    @Email(message = "Email format is invalid")
    @Size(max = 255, message = "Email must be at most 255 characters long")
    private String email;

    @Size(max = 25, message = "Phone number must be at most 25 characters long")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must contain 10-15 digits and may start with +"
    )
    private String phoneNumber;

    @Size(min = 8, max = 72, message = "Password must be 8-72 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, and 1 digit"
    )
    private String password;
}

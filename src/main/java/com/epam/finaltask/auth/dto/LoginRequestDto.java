package com.epam.finaltask.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 255, message = "Email must be at most 255 characters long")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(max = 72, message = "Password must be at most 72 characters long")
    private String password;
}

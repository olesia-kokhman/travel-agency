package com.epam.finaltask.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmDto {
    @NotBlank
    private String token;

    @NotBlank @Size(min = 8, max = 72)
    private String newPassword;
}

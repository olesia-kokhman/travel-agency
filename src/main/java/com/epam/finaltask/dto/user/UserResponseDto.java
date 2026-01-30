package com.epam.finaltask.dto.user;

import com.epam.finaltask.model.enums.UserRole;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {

    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean active;
    private BigDecimal balance;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

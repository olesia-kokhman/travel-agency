package com.epam.finaltask.dto.user;

import com.epam.finaltask.dto.order.OrderResponseDto;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

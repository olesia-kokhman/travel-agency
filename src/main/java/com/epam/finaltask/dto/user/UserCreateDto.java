package com.epam.finaltask.dto.user;

import com.epam.finaltask.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean active;
    private BigDecimal balance;
    private UserRole role;

}

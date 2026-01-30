package com.epam.finaltask.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateProfileDto {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;
    private BigDecimal balance;

}

package com.epam.finaltask.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDto {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;
}

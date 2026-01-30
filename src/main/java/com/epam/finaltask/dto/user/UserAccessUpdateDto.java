package com.epam.finaltask.dto.user;

import com.epam.finaltask.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccessUpdateDto {

    private Boolean active;
    private UserRole role;

}

package com.epam.finaltask.dto.user;

import com.epam.finaltask.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessUpdateDto {

    private Boolean active;
    private UserRole role;

}

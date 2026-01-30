package com.epam.finaltask.auth.dto;

import lombok.Data;

@Data
public class JwtResponseDto {
    private String jwtAccessToken;
    private String jwtRefreshToken;
}

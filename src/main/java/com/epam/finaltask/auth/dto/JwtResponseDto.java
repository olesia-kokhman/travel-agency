package com.epam.finaltask.auth.dto;

import lombok.Setter;

@Setter
public class JwtResponseDto {
    private String jwtAccessToken;
    private String jwtRefreshToken;
}

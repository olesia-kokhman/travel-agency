package com.epam.finaltask.auth;

import com.epam.finaltask.auth.dto.*;
import com.epam.finaltask.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtResponseDto>> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtResponseDto>> refresh(@Valid @RequestBody RefreshRequestDto requestDto) {
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequestDto requestDto) {
        return null;
    }

}

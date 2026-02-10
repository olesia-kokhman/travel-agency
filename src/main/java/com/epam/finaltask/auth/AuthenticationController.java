package com.epam.finaltask.auth;

import com.epam.finaltask.auth.dto.*;
import com.epam.finaltask.dto.apiresponse.ApiSuccessResponse;
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
    public ResponseEntity<ApiSuccessResponse<JwtResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "User is logged in",
                authenticationService.login(requestDto)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<JwtResponseDto>> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        authenticationService.register(requestDto);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "User is registered",
                null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiSuccessResponse<JwtResponseDto>> refresh(@Valid @RequestBody RefreshRequestDto requestDto) {
        JwtResponseDto dto = authenticationService.refresh(requestDto);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Token refreshed", dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiSuccessResponse<Void>> logout(@Valid @RequestBody RefreshRequestDto requestDto) {
        authenticationService.logout(requestDto);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Logged out", null));
    }

}

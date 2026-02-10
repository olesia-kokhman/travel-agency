package com.epam.finaltask.auth;

import com.epam.finaltask.auth.dto.PasswordResetConfirmDto;
import com.epam.finaltask.auth.dto.PasswordResetRequestDto;
import com.epam.finaltask.dto.apiresponse.ApiSuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password-reset")
@RequiredArgsConstructor
public class PasswordResetTokenController {

    private final PasswordResetTokenService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<ApiSuccessResponse<Void>> request(@RequestBody @Valid PasswordResetRequestDto dto) {
        passwordResetService.request(dto.getEmail());
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "If account exists, email was sent", null));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiSuccessResponse<Void>> confirm(@RequestBody @Valid PasswordResetConfirmDto dto) {
        passwordResetService.confirm(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Password reset successful", null));
    }
}

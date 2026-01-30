package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.user.UserAccessUpdateDto;
import com.epam.finaltask.dto.user.UserCreateDto;
import com.epam.finaltask.dto.user.UserResponseDto;
import com.epam.finaltask.dto.user.UserUpdateProfileDto;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(new ApiResponse<>("OK", "User is successfully created",
               userService.createUser(userCreateDto) ));
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable("user_id") UUID userId,
            @Valid @RequestBody UserAccessUpdateDto userAccessUpdateDto) {
        return ResponseEntity.ok(new ApiResponse<>("OK", "User is successfully updated",
                userService.updateUser(userId, userAccessUpdateDto)));
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("user_id") UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("OK", "User is successfully deleted",
                null));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable("user_id") UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>("OK", "User is successfully read",
                userService.getById(userId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>("OK", "Users are successfully read",
                userService.getAll()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiResponse<>("OK", "Me is successfully read",
                userService.getById(userDetails.getId())));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserUpdateProfileDto userUpdateProfileDto) {

        return ResponseEntity.ok(new ApiResponse<>("OK",  "User is successfully updated",
                userService.updateProfile(userDetails.getId(), userUpdateProfileDto)));
    }



}

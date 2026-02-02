package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiSuccessResponse;
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

    @PostMapping // admin
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "User is successfully created",
               userService.createUser(userCreateDto) ));
    }

    @PatchMapping("/{user_id}") // admin
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> updateUser(
            @PathVariable("user_id") UUID userId,
            @Valid @RequestBody UserAccessUpdateDto userAccessUpdateDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "User is successfully updated",
                userService.updateUser(userId, userAccessUpdateDto)));
    }

    @DeleteMapping("/{user_id}") // admin
    public ResponseEntity<ApiSuccessResponse<Void>> deleteUser(@PathVariable("user_id") UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "User is successfully deleted",
                null));
    }

    @GetMapping("/{user_id}") //admin
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> getUser(@PathVariable("user_id") UUID userId) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "User is successfully read",
                userService.getById(userId)));
    }

    @GetMapping // admin
    public ResponseEntity<ApiSuccessResponse<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Users are successfully read",
                userService.getAll()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Me is successfully read",
                userService.getById(userDetails.getId())));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserUpdateProfileDto userUpdateProfileDto) {

        return ResponseEntity.ok(new ApiSuccessResponse<>("OK",  "User is successfully updated",
                userService.updateProfile(userDetails.getId(), userUpdateProfileDto)));
    }

}

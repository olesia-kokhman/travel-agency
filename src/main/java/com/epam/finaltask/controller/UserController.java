package com.epam.finaltask.controller;

import com.epam.finaltask.dto.apiresponse.ApiPageResponse;
import com.epam.finaltask.dto.apiresponse.ApiSuccessResponse;
import com.epam.finaltask.dto.user.UserAccessUpdateDto;
import com.epam.finaltask.dto.user.UserCreateDto;
import com.epam.finaltask.dto.user.UserResponseDto;
import com.epam.finaltask.dto.user.UserUpdateProfileDto;
import com.epam.finaltask.repository.specifications.filters.UserFilter;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping // admin
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserResponseDto created = userService.createUser(userCreateDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiSuccessResponse<>(201, "User created successfully.", created));
    }

    @PatchMapping("/{user_id}") // admin
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> updateUser(
            @PathVariable("user_id") UUID userId,
            @Valid @RequestBody UserAccessUpdateDto userAccessUpdateDto) {
        UserResponseDto updated = userService.updateUser(userId, userAccessUpdateDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "User updated successfully.", updated));
    }

    @DeleteMapping("/{user_id}") // admin
    public ResponseEntity<ApiSuccessResponse<Void>> deleteUser(@PathVariable("user_id") UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{user_id}") //admin
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> getUser(@PathVariable("user_id") UUID userId) {
        UserResponseDto dto = userService.getById(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "User fetched successfully.", dto));
    }

    @GetMapping // admin
    public ResponseEntity<ApiPageResponse<UserResponseDto>> getAllUsers(
            @Valid @ModelAttribute UserFilter filter,
            Pageable pageable) {
        Page<UserResponseDto> page = userService.getAll(filter, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiPageResponse.from(page, 200, "Users fetched successfully."));
    }


    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponseDto dto = userService.getMe(userDetails.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Profile fetched successfully.", dto));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiSuccessResponse<UserResponseDto>> updateUserProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserUpdateProfileDto userUpdateProfileDto) {
        UserResponseDto updated = userService.updateProfile(userDetails.getId(), userUpdateProfileDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Profile updated successfully.", updated));
    }

}

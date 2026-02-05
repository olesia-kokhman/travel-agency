package com.epam.finaltask.controller;

import com.epam.finaltask.dto.apiresponse.ApiSuccessResponse;
import com.epam.finaltask.dto.review.ReviewCreateDto;
import com.epam.finaltask.dto.review.ReviewResponseDto;
import com.epam.finaltask.dto.review.ReviewUpdateDto;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable UUID orderId,
            @Valid @RequestBody ReviewCreateDto dto) {

        ReviewResponseDto created = reviewService.createReview(userDetails.getId(), orderId, dto);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Review created", created));
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> updateReview(
            @AuthenticationPrincipal UserDetailsImpl user,
            @PathVariable UUID reviewId,
            @Valid @RequestBody ReviewUpdateDto dto) {

        ReviewResponseDto updated = reviewService.updateReview(user.getId(), reviewId, dto);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Review updated", updated));
    }


    @GetMapping("/tours/{tourId}/reviews")
    public ResponseEntity<ApiSuccessResponse<List<ReviewResponseDto>>> getAllByTour(
            @PathVariable UUID tourId) {
        List<ReviewResponseDto> reviews = reviewService.getAllByTour(tourId);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Reviews list", reviews));
    }

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<ApiSuccessResponse<List<ReviewResponseDto>>> getAllByUser(
            @PathVariable UUID userId) {
        List<ReviewResponseDto> reviews = reviewService.getAllByUser(userId);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Reviews list", reviews));
    }

}

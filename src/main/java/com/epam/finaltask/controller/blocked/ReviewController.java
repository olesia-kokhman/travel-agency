package com.epam.finaltask.controller.blocked;

import com.epam.finaltask.dto.ApiSuccessResponse;
import com.epam.finaltask.dto.order.review.ReviewRequestDto;
import com.epam.finaltask.dto.order.review.ReviewResponseDto;
import com.epam.finaltask.service.blocked.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{user_id}/orders/{order_id}/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> readReviewByOrder(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> createReview(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping
    public ResponseEntity<ApiSuccessResponse<ReviewResponseDto>> updateReview(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<ApiSuccessResponse<Void>> deleteReview(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }
}

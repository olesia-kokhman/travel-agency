package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.order.extra.OrderExtraRequestDto;
import com.epam.finaltask.dto.order.extra.OrderExtraResponseDto;
import com.epam.finaltask.service.OrderExtraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{user_id}/orders/{order_id}/extras")
@RequiredArgsConstructor
public class OrderExtraController {

    private final OrderExtraService orderExtraService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderExtraResponseDto>>> readAllExtrasByOrder(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderExtraResponseDto>> readExtraById(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderExtraResponseDto>> createExtra(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody OrderExtraRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderExtraResponseDto>> updateExtra(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @PathVariable("id") UUID id,
            @Valid @RequestBody OrderExtraRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExtra(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }
}

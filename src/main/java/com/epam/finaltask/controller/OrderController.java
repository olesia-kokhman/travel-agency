package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.order.OrderRequestDto;
import com.epam.finaltask.dto.order.OrderResponseDto;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{user_id}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> readAllOrdersByUser(
            @PathVariable("user_id") UUID userId
    ) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> readOrderById(
            @PathVariable("user_id") UUID userId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @PathVariable("user_id") UUID userId,
            @Valid @RequestBody OrderRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(
            @PathVariable("user_id") UUID userId,
            @PathVariable("id") UUID id,
            @Valid @RequestBody OrderRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @PathVariable("user_id") UUID userId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(
            @PathVariable("user_id") UUID userId,
            @PathVariable("id") UUID id,
            @RequestParam("status") OrderStatus status
    ) {
        return null;
    }
}

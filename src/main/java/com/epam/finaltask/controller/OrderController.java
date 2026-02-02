package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiSuccessResponse;
import com.epam.finaltask.dto.order.OrderCreateDto;
import com.epam.finaltask.dto.order.OrderResponseDto;
import com.epam.finaltask.dto.order.OrderStatusUpdateDto;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping // admin
    public ResponseEntity<ApiSuccessResponse<List<OrderResponseDto>>> getAllOrders() {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "MESSAGE", orderService.getAll()));
    }

    @GetMapping("/me/{user_id}") // user and admin
    public ResponseEntity<ApiSuccessResponse<List<OrderResponseDto>>> getAllOrdersByUser(@PathVariable("user_id") UUID userId) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "MESSAGE", orderService.getAll(userId)));
    }

    @GetMapping("/me/order/{order_id}") // for user
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> getOrderById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable("order_id") UUID orderId) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "MESSAGE",
                orderService.getById(userDetails.getId(), orderId)));
    }

    @GetMapping("/{order_id}") // for admin
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> getOrderById(@PathVariable("order_id") UUID orderId) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "MESSAGE",
               orderService.getById(orderId)));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                            @Valid @RequestBody OrderCreateDto createDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "MESSAGE",
               orderService.create(userDetails.getId(), createDto)));
    }

    @PatchMapping("/{order_id}/status")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> updateOrderStatus(
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody OrderStatusUpdateDto orderStatusUpdateDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "MESSAGE",
                orderService.updateStatus(orderId, orderStatusUpdateDto)));
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteOrder(@PathVariable("order_id") UUID orderId) {
        orderService.delete(orderId);
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "MESSAGE", null));
    }

}

package com.epam.finaltask.controller;

import com.epam.finaltask.dto.apiresponse.ApiPageResponse;
import com.epam.finaltask.dto.apiresponse.ApiSuccessResponse;
import com.epam.finaltask.dto.order.AdminOrderResponseDto;
import com.epam.finaltask.dto.order.OrderCreateDto;
import com.epam.finaltask.dto.order.OrderResponseDto;
import com.epam.finaltask.dto.order.OrderStatusUpdateDto;
import com.epam.finaltask.repository.specifications.filters.OrderFilter;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.service.OrderService;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping // admin
    public ResponseEntity<ApiPageResponse<AdminOrderResponseDto>> getAllOrders(
            @Valid @ModelAttribute OrderFilter orderFilter,
            Pageable pageable) {
        Page<AdminOrderResponseDto> page = orderService.getAll(orderFilter, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiPageResponse.from(page, 200, "Orders fetched successfully."));
    }

    @GetMapping("/me/{user_id}") // user and admin
    public ResponseEntity<ApiPageResponse<OrderResponseDto>> getAllOrdersByUser(
            @PathVariable("user_id") UUID userId,
            @Valid @ModelAttribute OrderFilter orderFilter,
            Pageable pageable) {

        Page<OrderResponseDto> page = orderService.getAllByUser(userId, orderFilter, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiPageResponse.from(page, 200, "User orders fetched successfully."));
    }

    @GetMapping("/me/order/{order_id}") // for user
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> getOrderById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable("order_id") UUID orderId) {
        OrderResponseDto dto = orderService.getById(userDetails.getId(), orderId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Order fetched successfully.", dto));
    }

    @GetMapping("/{order_id}") // for admin
    public ResponseEntity<ApiSuccessResponse<AdminOrderResponseDto>> getOrderById(@PathVariable("order_id") UUID orderId) {
        AdminOrderResponseDto dto = orderService.getById(orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Order fetched successfully.", dto));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> createOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                            @Valid @RequestBody OrderCreateDto createDto) {
        OrderResponseDto created = orderService.create(userDetails.getId(), createDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/orders/me/order/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiSuccessResponse<>(201, "Order created successfully.", created));
    }

    @PatchMapping("/{order_id}/status")
    public ResponseEntity<ApiSuccessResponse<OrderResponseDto>> updateOrderStatus(
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody OrderStatusUpdateDto orderStatusUpdateDto) {
        OrderResponseDto updated = orderService.updateStatus(orderId, orderStatusUpdateDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Order status updated successfully.", updated));
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteOrder(@PathVariable("order_id") UUID orderId) {
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }

}

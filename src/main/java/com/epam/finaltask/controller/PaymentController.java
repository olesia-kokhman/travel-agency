package com.epam.finaltask.controller;

import com.epam.finaltask.dto.apiresponse.ApiPageResponse;
import com.epam.finaltask.dto.apiresponse.ApiSuccessResponse;
import com.epam.finaltask.dto.payment.PaymentRequestDto;
import com.epam.finaltask.dto.payment.PaymentResponseDto;
import com.epam.finaltask.repository.specifications.filters.PaymentFilter;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/me/orders/{orderId}")
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> create(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable UUID orderId,
            @Valid @RequestBody PaymentRequestDto paymentRequestDto
    ) {
        PaymentResponseDto result = paymentService.create(principal.getId(), orderId, paymentRequestDto);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Payment is successfully created", result));
    }

    @GetMapping("/me/orders/{orderId}")
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> getPayment(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable UUID orderId) {
        PaymentResponseDto result = paymentService.getPayment(principal.getId(), orderId);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Payment is successfully fetched", result));
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> getPayment(
            @PathVariable UUID orderId) {
        PaymentResponseDto result = paymentService.getPayment(orderId);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Payment is successfully fetched", result));
    }

    @GetMapping("/users/{userId}/payments")
    public ResponseEntity<ApiPageResponse<PaymentResponseDto>> getAllByUser(
            @PathVariable UUID userId,
            @Valid @ModelAttribute PaymentFilter filter,
            Pageable pageable
    ) {
        Page<PaymentResponseDto> page = paymentService.getAllByUser(userId, filter, pageable);
        return ResponseEntity.ok(ApiPageResponse.from(page, 200, "Payments are successfully fetched"));
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<PaymentResponseDto>> getAll(
            @Valid @ModelAttribute PaymentFilter filter,
            Pageable pageable
    ) {
        Page<PaymentResponseDto> page = paymentService.getAll(filter, pageable);
        return ResponseEntity.ok(ApiPageResponse.from(page, 200, "Payments are successfully fetched"));
    }

}

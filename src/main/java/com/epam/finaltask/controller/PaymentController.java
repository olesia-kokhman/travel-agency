package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiSuccessResponse;
import com.epam.finaltask.dto.payment.PaymentRequestDto;
import com.epam.finaltask.dto.payment.PaymentResponseDto;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/me/orders/{orderId}")
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> create(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable UUID orderId,
            @Valid @RequestBody PaymentRequestDto paymentRequestDto
    ) {
        PaymentResponseDto result = paymentService.create(principal.getId(), orderId, paymentRequestDto);
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Payment is successfully created", result));
    }

    @GetMapping("/me/orders/{orderId}")
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> getPayment(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @PathVariable UUID orderId
    ) {
        PaymentResponseDto result = paymentService.getPayment(principal.getId(), orderId);
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Payment is successfully fetched", result));
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> getPayment(
            @PathVariable UUID orderId
    ) {
        PaymentResponseDto result = paymentService.getPayment(orderId);
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Payment is successfully fetched", result));
    }
}

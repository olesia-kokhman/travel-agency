package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiSuccessResponse;
import com.epam.finaltask.dto.payment.PaymentRequestDto;
import com.epam.finaltask.dto.payment.PaymentResponseDto;
import com.epam.finaltask.model.enums.PaymentStatus;
import com.epam.finaltask.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{user_id}/orders/{order_id}/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> readPaymentByOrder(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> createPayment(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody PaymentRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> updatePayment(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody PaymentRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<ApiSuccessResponse<Void>> deletePayment(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

    @PatchMapping("/status")
    public ResponseEntity<ApiSuccessResponse<PaymentResponseDto>> updatePaymentStatus(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @RequestParam("status") PaymentStatus status
    ) {
        return null;
    }
}

package com.epam.finaltask.dto.order;

import com.epam.finaltask.dto.payment.PaymentResponseDto;
import com.epam.finaltask.dto.review.ReviewResponseDto;
import com.epam.finaltask.dto.user.UserResponseDto;
import com.epam.finaltask.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderResponseDto {

    private UUID id;

    private String orderNumber;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private UUID tourId;

    private UserResponseDto user;
    private ReviewResponseDto review;
    private PaymentResponseDto payment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.epam.finaltask.service;

import com.epam.finaltask.dto.payment.PaymentRequestDto;
import com.epam.finaltask.dto.payment.PaymentResponseDto;
import com.epam.finaltask.exception.BusinessValidationException;
import com.epam.finaltask.exception.ResourceNotFoundException;
import com.epam.finaltask.exception.UserAccessDeniedException;
import com.epam.finaltask.mapper.PaymentMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.Payment;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.model.enums.PaymentStatus;
import com.epam.finaltask.repository.OrderRepository;
import com.epam.finaltask.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public PaymentResponseDto create(UUID userId, UUID orderId, PaymentRequestDto paymentRequestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (order.getStatus() != OrderStatus.REGISTERED) {
            throw new BusinessValidationException("Payment can be created only for REGISTERED order");
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new UserAccessDeniedException();
        }

        if (paymentRepository.existsByOrderId(orderId)) {
            throw new BusinessValidationException("Payment already exists for this order");
        }

        Payment payment = paymentMapper.toPayment(paymentRequestDto);
        payment.setOrder(order);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setFailureReason(null);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return paymentMapper.toPaymentResponseDto(paymentRepository.save(payment));
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional // for user
    public PaymentResponseDto getPayment(UUID userId, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new UserAccessDeniedException();
        }

        Payment payment = order.getPayment();

        if (payment == null) {
            throw new ResourceNotFoundException("Payment", orderId);
        }

        return paymentMapper.toPaymentResponseDto(payment);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Transactional // for admin
    public PaymentResponseDto getPayment(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        Payment payment = order.getPayment();

        if (payment == null) {
            throw new ResourceNotFoundException("Payment", orderId);
        }
        return paymentMapper.toPaymentResponseDto(payment);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllByUser(UUID userId) {
        return paymentRepository.findAllByUserId(userId).stream().map(paymentMapper::toPaymentResponseDto).toList();
    }


}
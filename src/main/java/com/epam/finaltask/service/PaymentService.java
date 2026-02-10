package com.epam.finaltask.service;

import com.epam.finaltask.dto.payment.PaymentRequestDto;
import com.epam.finaltask.dto.payment.PaymentResponseDto;
import com.epam.finaltask.exception.exceptions.BusinessValidationException;
import com.epam.finaltask.exception.exceptions.ResourceNotFoundException;
import com.epam.finaltask.exception.exceptions.UserAccessDeniedException;
import com.epam.finaltask.mapper.PaymentMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.Payment;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.model.enums.PaymentStatus;
import com.epam.finaltask.repository.OrderRepository;
import com.epam.finaltask.repository.PaymentRepository;
import com.epam.finaltask.repository.specifications.PaymentSpecification;
import com.epam.finaltask.repository.specifications.filters.PaymentFilter;
import com.epam.finaltask.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.epam.finaltask.util.PageableUtils.withDefaultSort;

@Service
@RequiredArgsConstructor
@Slf4j
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

        OrderStatus oldStatus = order.getStatus();

        Payment payment = paymentMapper.toPayment(paymentRequestDto);
        payment.setOrder(order);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        payment.setFailureReason(null);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        Payment savedPayment = paymentRepository.save(payment);

        log.info("BUSINESS paymentCreated paymentId={} orderId={} userId={} amount={} status={} paidAt={}",
                savedPayment.getId(),
                orderId,
                userId,
                order.getTotalAmount(),
                savedPayment.getStatus(),
                savedPayment.getPaidAt()
        );

        log.info("BUSINESS orderStatusChangedByPayment orderId={} from={} to={}",
                orderId, oldStatus, OrderStatus.PAID
        );

        return paymentMapper.toPaymentResponseDto(savedPayment);
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true) // for admin
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


    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getAll(PaymentFilter filter, Pageable pageable) {
        Page<Payment> page = paymentRepository.findAll(PaymentSpecification.build(filter), pageable);
        return page.map(paymentMapper::toPaymentResponseDto);
    }


}
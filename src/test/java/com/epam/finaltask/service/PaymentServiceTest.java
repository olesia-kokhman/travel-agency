package com.epam.finaltask.service;

import com.epam.finaltask.dto.payment.PaymentRequestDto;
import com.epam.finaltask.dto.payment.PaymentResponseDto;
import com.epam.finaltask.exception.exceptions.BusinessValidationException;
import com.epam.finaltask.exception.exceptions.ResourceNotFoundException;
import com.epam.finaltask.exception.exceptions.UserAccessDeniedException;
import com.epam.finaltask.mapper.PaymentMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.Payment;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.model.enums.PaymentMethod;
import com.epam.finaltask.model.enums.PaymentStatus;
import com.epam.finaltask.repository.OrderRepository;
import com.epam.finaltask.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void create_whenOrderNotFound_throwsResourceNotFound() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.create(userId, orderId, new PaymentRequestDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentRepository, paymentMapper);
    }

    @Test
    void create_whenOrderStatusNotRegistered_throwsBusinessValidation() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessValidationException.class,
                () -> paymentService.create(userId, orderId, new PaymentRequestDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentRepository, paymentMapper);
    }

    @Test
    void create_whenUserIsNotOwner_throwsUserAccessDenied() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User otherUser = new User();
        otherUser.setId(UUID.randomUUID());

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.REGISTERED);
        order.setUser(otherUser);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(UserAccessDeniedException.class,
                () -> paymentService.create(userId, orderId, new PaymentRequestDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentRepository, paymentMapper);
    }

    @Test
    void create_whenPaymentAlreadyExists_throwsBusinessValidation() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.REGISTERED);
        order.setUser(user);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.existsByOrderId(orderId)).thenReturn(true);

        assertThrows(BusinessValidationException.class,
                () -> paymentService.create(userId, orderId, new PaymentRequestDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentRepository, times(1)).existsByOrderId(orderId);
        verifyNoMoreInteractions(paymentRepository);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void create_happyPath_setsFields_updatesOrderStatus_savesAndReturnsDto() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.REGISTERED);
        order.setUser(user);

        PaymentRequestDto req = new PaymentRequestDto();
        req.setPaymentMethod(PaymentMethod.CARD);
        req.setAmount(new BigDecimal("100.00"));

        Payment mappedPayment = new Payment();
        mappedPayment.setPaymentMethod(req.getPaymentMethod());
        mappedPayment.setAmount(req.getAmount());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentRepository.existsByOrderId(orderId)).thenReturn(false);
        when(paymentMapper.toPayment(req)).thenReturn(mappedPayment);

        Payment savedPayment = new Payment();
        savedPayment.setId(UUID.randomUUID());
        savedPayment.setPaymentMethod(req.getPaymentMethod());
        savedPayment.setAmount(req.getAmount());
        savedPayment.setStatus(PaymentStatus.SUCCESS);
        savedPayment.setOrder(order);

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponseDto resp = new PaymentResponseDto();
        resp.setId(savedPayment.getId());
        resp.setPaymentMethod(savedPayment.getPaymentMethod());
        resp.setAmount(savedPayment.getAmount());
        resp.setStatus(PaymentStatus.SUCCESS);
        resp.setOrderId(orderId);

        when(paymentMapper.toPaymentResponseDto(savedPayment)).thenReturn(resp);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        // When
        PaymentResponseDto result = paymentService.create(userId, orderId, req);

        // Then
        assertNotNull(result);
        assertEquals(resp.getId(), result.getId());
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());
        assertEquals(PaymentMethod.CARD, result.getPaymentMethod());
        assertEquals(new BigDecimal("100.00"), result.getAmount());

        assertEquals(OrderStatus.PAID, order.getStatus());

        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentRepository, times(1)).existsByOrderId(orderId);
        verify(paymentMapper, times(1)).toPayment(req);

        verify(orderRepository, times(1)).save(orderCaptor.capture());
        assertSame(order, orderCaptor.getValue());
        assertEquals(OrderStatus.PAID, orderCaptor.getValue().getStatus());

        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        Payment toSave = paymentCaptor.getValue();

        assertSame(order, toSave.getOrder());
        assertEquals(PaymentStatus.SUCCESS, toSave.getStatus());
        assertNotNull(toSave.getPaidAt(), "paidAt must be set");
        assertNull(toSave.getFailureReason(), "failureReason must be null");
        assertEquals(PaymentMethod.CARD, toSave.getPaymentMethod());
        assertEquals(new BigDecimal("100.00"), toSave.getAmount());

        verify(paymentMapper, times(1)).toPaymentResponseDto(savedPayment);
    }

    @Test
    void getPayment_userScoped_whenOrderNotFound_throwsResourceNotFound() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPayment(userId, orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void getPayment_userScoped_whenNotOwner_throwsUserAccessDenied() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User other = new User();
        other.setId(UUID.randomUUID());

        Order order = new Order();
        order.setId(orderId);
        order.setUser(other);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(UserAccessDeniedException.class, () -> paymentService.getPayment(userId, orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void getPayment_userScoped_whenPaymentNull_throwsResourceNotFound() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setPayment(null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPayment(userId, orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void getPayment_userScoped_happyPath_returnsMappedDto() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setStatus(PaymentStatus.SUCCESS);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setPayment(payment);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setStatus(PaymentStatus.SUCCESS);

        when(paymentMapper.toPaymentResponseDto(payment)).thenReturn(dto);

        PaymentResponseDto result = paymentService.getPayment(userId, orderId);

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        assertEquals(PaymentStatus.SUCCESS, result.getStatus());

        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentMapper, times(1)).toPaymentResponseDto(payment);
    }

    @Test
    void getPayment_admin_whenOrderNotFound_throwsResourceNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPayment(orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void getPayment_admin_whenPaymentNull_throwsResourceNotFound() {
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setPayment(null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPayment(orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(paymentMapper);
    }

    @Test
    void getPayment_admin_happyPath_returnsMappedDto() {
        UUID orderId = UUID.randomUUID();

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());

        Order order = new Order();
        order.setId(orderId);
        order.setPayment(payment);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());

        when(paymentMapper.toPaymentResponseDto(payment)).thenReturn(dto);

        PaymentResponseDto result = paymentService.getPayment(orderId);

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());

        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentMapper, times(1)).toPaymentResponseDto(payment);
    }

}

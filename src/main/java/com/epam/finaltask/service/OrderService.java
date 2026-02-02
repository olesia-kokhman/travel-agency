package com.epam.finaltask.service;

import com.epam.finaltask.dto.order.OrderCreateDto;
import com.epam.finaltask.dto.order.OrderResponseDto;
import com.epam.finaltask.dto.order.OrderStatusUpdateDto;
import com.epam.finaltask.exception.ResourceNotFoundException;
import com.epam.finaltask.exception.UserNotFoundException;
import com.epam.finaltask.mapper.OrderMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.repository.OrderRepository;
import com.epam.finaltask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    @PreAuthorize("#userId == authentication.principal.id or hasAnyRole('ADMIN','MANAGER')")
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAll(UUID userId) {
        if(!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream().map(orderMapper::toOrderResponseDto).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAll() {
        return orderRepository.findAll().stream().map(orderMapper::toOrderResponseDto).toList();
    }

    @PreAuthorize("#userId == authentication.principal.id or hasAnyRole('ADMIN','MANAGER')")
    @Transactional(readOnly = true)
    public OrderResponseDto getById(UUID userId, UUID orderId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        return orderMapper.toOrderResponseDto(order);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Transactional(readOnly = true)
    public OrderResponseDto getById(UUID orderId) {
        return orderMapper.toOrderResponseDto(orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Order", orderId)));
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public OrderResponseDto create(UUID userId, OrderCreateDto orderCreateDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Order order = orderMapper.toOrder(orderCreateDto);
        order.setOrderNumber("some-number"); // need to change
        order.setTotalAmount(BigDecimal.ZERO); // need to change
        order.setStatus(OrderStatus.REGISTERED);
        order.setUser(user);

        return orderMapper.toOrderResponseDto(orderRepository.save(order));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Transactional
    public OrderResponseDto updateStatus(UUID orderId, OrderStatusUpdateDto orderStatusUpdateDto) {
        Order currentOrder = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Order", orderId));

        currentOrder.setStatus(orderStatusUpdateDto.getStatus());
        return orderMapper.toOrderResponseDto(orderRepository.save(currentOrder));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID orderId) {

        try {
            orderRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Order", orderId);
        }

    }

}

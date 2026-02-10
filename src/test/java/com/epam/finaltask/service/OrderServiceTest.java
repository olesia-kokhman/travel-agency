package com.epam.finaltask.service;

import com.epam.finaltask.dto.order.*;
import com.epam.finaltask.exception.exceptions.BusinessValidationException;
import com.epam.finaltask.exception.exceptions.ResourceNotFoundException;
import com.epam.finaltask.exception.exceptions.UserNotFoundException;
import com.epam.finaltask.mapper.OrderMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.Tour;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.util.OrderNumberGenerator;
import com.epam.finaltask.repository.OrderRepository;
import com.epam.finaltask.repository.TourRepository;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.repository.specifications.filters.OrderFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private UserRepository userRepository;
    @Mock private TourRepository tourRepository;
    @Mock private OrderNumberGenerator orderNumberGenerator;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getAllByUser_whenUserNotExists_throwsUserNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        // When / Then
        assertThrows(UserNotFoundException.class,
                () -> orderService.getAllByUser(userId, new OrderFilter(), PageRequest.of(0, 10)));

        verify(userRepository, times(1)).existsById(userId);
        verifyNoInteractions(orderRepository, orderMapper);
    }

    @Test
    void getById_userScoped_whenUserNotExists_throwsUserNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        // When / Then
        assertThrows(UserNotFoundException.class, () -> orderService.getById(userId, orderId));

        verify(userRepository, times(1)).existsById(userId);
        verifyNoInteractions(orderRepository, orderMapper);
    }

    @Test
    void getById_userScoped_whenOrderNotFound_throwsResourceNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> orderService.getById(userId, orderId));

        verify(userRepository, times(1)).existsById(userId);
        verify(orderRepository, times(1)).findByIdAndUserId(orderId, userId);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void getById_userScoped_whenFound_returnsMappedDto() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(true);

        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findByIdAndUserId(orderId, userId)).thenReturn(Optional.of(order));

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(orderId);
        when(orderMapper.toOrderResponseDto(order)).thenReturn(dto);

        // When
        OrderResponseDto result = orderService.getById(userId, orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getId());

        verify(userRepository, times(1)).existsById(userId);
        verify(orderRepository, times(1)).findByIdAndUserId(orderId, userId);
        verify(orderMapper, times(1)).toOrderResponseDto(order);
    }

    @Test
    void getById_admin_whenNotFound_throwsResourceNotFound() {
        // Given
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> orderService.getById(orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void getById_admin_whenFound_returnsMappedAdminDto() {
        // Given
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        AdminOrderResponseDto dto = new AdminOrderResponseDto();
        dto.setId(orderId);
        when(orderMapper.toAdminOrderResponseDto(order)).thenReturn(dto);

        // When
        AdminOrderResponseDto result = orderService.getById(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getId());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderMapper, times(1)).toAdminOrderResponseDto(order);
    }

    @Test
    void create_whenUserNotFound_throwsUserNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        OrderCreateDto createDto = new OrderCreateDto(tourId);

        // When / Then
        assertThrows(UserNotFoundException.class, () -> orderService.create(userId, createDto));

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(tourRepository, orderRepository, orderMapper, orderNumberGenerator);
    }

    @Test
    void create_whenTourNotFound_throwsResourceNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        OrderCreateDto createDto = new OrderCreateDto(tourId);

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> orderService.create(userId, createDto));

        verify(userRepository, times(1)).findById(userId);
        verify(tourRepository, times(1)).findById(tourId);
        verifyNoInteractions(orderRepository, orderMapper, orderNumberGenerator);
    }

    @Test
    void create_whenTourInactive_throwsBusinessValidation() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Tour tour = new Tour();
        tour.setId(tourId);
        tour.setActive(false);
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));

        // When / Then
        assertThrows(BusinessValidationException.class,
                () -> orderService.create(userId, new OrderCreateDto(tourId)));

        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper, orderNumberGenerator);
    }

    @Test
    void create_whenCapacityNull_throwsBusinessValidation() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Tour tour = new Tour();
        tour.setId(tourId);
        tour.setActive(true);
        tour.setCapacity(null);
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));

        // When / Then
        assertThrows(BusinessValidationException.class,
                () -> orderService.create(userId, new OrderCreateDto(tourId)));

        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper, orderNumberGenerator);
    }

    @Test
    void create_whenCapacityZeroOrLess_throwsBusinessValidation() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Tour tour = new Tour();
        tour.setId(tourId);
        tour.setActive(true);
        tour.setCapacity(0);
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));

        // When / Then
        assertThrows(BusinessValidationException.class,
                () -> orderService.create(userId, new OrderCreateDto(tourId)));

        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper, orderNumberGenerator);
    }

    @Test
    void create_happyPath_decrementsCapacity_setsFields_andReturnsMappedDto() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID tourId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Tour tour = new Tour();
        tour.setId(tourId);
        tour.setActive(true);
        tour.setCapacity(5);
        tour.setPrice(new BigDecimal("123.45"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));
        when(orderNumberGenerator.generateUniqueOrderNumber()).thenReturn("ORD-0001");
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        Order saved = new Order();
        saved.setId(UUID.randomUUID());
        saved.setOrderNumber("ORD-0001");
        saved.setUser(user);
        saved.setTour(tour);
        saved.setTotalAmount(tour.getPrice());
        saved.setStatus(OrderStatus.REGISTERED);

        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderResponseDto mapped = new OrderResponseDto();
        mapped.setId(saved.getId());
        mapped.setOrderNumber(saved.getOrderNumber());
        mapped.setStatus(saved.getStatus());
        mapped.setTotalAmount(saved.getTotalAmount());
        mapped.setTourId(tourId);
        mapped.setCreatedAt(LocalDateTime.now());

        when(orderMapper.toOrderResponseDto(saved)).thenReturn(mapped);

        // When
        OrderResponseDto result = orderService.create(userId, new OrderCreateDto(tourId));

        // Then
        assertNotNull(result);
        assertEquals("ORD-0001", result.getOrderNumber());
        assertEquals(OrderStatus.REGISTERED, result.getStatus());
        assertEquals(new BigDecimal("123.45"), result.getTotalAmount());
        assertEquals(tourId, result.getTourId());

        assertEquals(4, tour.getCapacity());

        verify(orderNumberGenerator, times(1)).generateUniqueOrderNumber();
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order toSave = orderCaptor.getValue();

        assertEquals("ORD-0001", toSave.getOrderNumber());
        assertSame(user, toSave.getUser());
        assertSame(tour, toSave.getTour());
        assertEquals(new BigDecimal("123.45"), toSave.getTotalAmount());
        assertEquals(OrderStatus.REGISTERED, toSave.getStatus());

        verify(orderMapper, times(1)).toOrderResponseDto(saved);
    }

    @Test
    void updateStatus_whenOrderNotFound_throwsResourceNotFound() {
        // Given
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class,
                () -> orderService.updateStatus(orderId, new OrderStatusUpdateDto(OrderStatus.PAID)));

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).save(any(Order.class));
        verifyNoInteractions(orderMapper);
    }

    @Test
    void updateStatus_happyPath_updatesStatus_saves_andReturnsMappedDto() {
        // Given
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.REGISTERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order saved = new Order();
        saved.setId(orderId);
        saved.setStatus(OrderStatus.PAID);

        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(orderId);
        dto.setStatus(OrderStatus.PAID);

        when(orderMapper.toOrderResponseDto(saved)).thenReturn(dto);

        // When
        OrderResponseDto result = orderService.updateStatus(orderId, new OrderStatusUpdateDto(OrderStatus.PAID));

        // Then
        assertNotNull(result);
        assertEquals(OrderStatus.PAID, result.getStatus());

        assertEquals(OrderStatus.PAID, order.getStatus());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).toOrderResponseDto(saved);
    }

    @Test
    void delete_whenDeleteByIdThrowsEmptyResult_translatesToResourceNotFound() {
        // Given
        UUID orderId = UUID.randomUUID();
        doThrow(new EmptyResultDataAccessException(1)).when(orderRepository).deleteById(orderId);

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> orderService.delete(orderId));

        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void delete_happyPath_callsRepository() {
        // Given
        UUID orderId = UUID.randomUUID();
        doNothing().when(orderRepository).deleteById(orderId);

        // When
        orderService.delete(orderId);

        // Then
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}

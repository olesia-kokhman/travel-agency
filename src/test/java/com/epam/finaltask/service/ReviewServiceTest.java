package com.epam.finaltask.service;

import com.epam.finaltask.dto.review.ReviewCreateDto;
import com.epam.finaltask.dto.review.ReviewResponseDto;
import com.epam.finaltask.dto.review.ReviewUpdateDto;
import com.epam.finaltask.exception.exceptions.BusinessValidationException;
import com.epam.finaltask.exception.exceptions.ResourceNotFoundException;
import com.epam.finaltask.exception.exceptions.UserAccessDeniedException;
import com.epam.finaltask.mapper.ReviewMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.Review;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.repository.OrderRepository;
import com.epam.finaltask.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private ReviewMapper reviewMapper;
    @Mock private OrderRepository orderRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void createReview_whenOrderNotFound_throwsResourceNotFound() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.createReview(userId, orderId, new ReviewCreateDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(reviewRepository, reviewMapper);
    }

    @Test
    void createReview_whenNotOwner_throwsUserAccessDenied() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User other = new User();
        other.setId(UUID.randomUUID());

        Order order = new Order();
        order.setId(orderId);
        order.setUser(other);
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(UserAccessDeniedException.class,
                () -> reviewService.createReview(userId, orderId, new ReviewCreateDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(reviewRepository, reviewMapper);
    }

    @Test
    void createReview_whenOrderNotPaid_throwsBusinessValidation() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(OrderStatus.REGISTERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessValidationException.class,
                () -> reviewService.createReview(userId, orderId, new ReviewCreateDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoInteractions(reviewRepository, reviewMapper);
    }

    @Test
    void createReview_whenReviewAlreadyExists_throwsBusinessValidation() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(reviewRepository.existsByOrderId(orderId)).thenReturn(true);

        assertThrows(BusinessValidationException.class,
                () -> reviewService.createReview(userId, orderId, new ReviewCreateDto()));

        verify(orderRepository, times(1)).findById(orderId);
        verify(reviewRepository, times(1)).existsByOrderId(orderId);
        verifyNoMoreInteractions(reviewRepository);
        verifyNoInteractions(reviewMapper);
    }

    @Test
    void createReview_happyPath_setsOrder_savesAndReturnsMappedDto() {
        UUID userId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(reviewRepository.existsByOrderId(orderId)).thenReturn(false);

        ReviewCreateDto createDto = new ReviewCreateDto();
        createDto.setComment("Nice tour");
        createDto.setRating(5);

        Review mapped = new Review();
        mapped.setComment(createDto.getComment());
        mapped.setRating(createDto.getRating());

        when(reviewMapper.toReview(createDto)).thenReturn(mapped);

        Review saved = new Review();
        saved.setId(UUID.randomUUID());
        saved.setComment(mapped.getComment());
        saved.setRating(mapped.getRating());
        saved.setOrder(order);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);

        when(reviewRepository.save(any(Review.class))).thenReturn(saved);

        ReviewResponseDto resp = new ReviewResponseDto();
        resp.setId(saved.getId());
        resp.setComment(saved.getComment());
        resp.setRating(saved.getRating());
        resp.setOrderId(orderId);

        when(reviewMapper.toReviewResponseDto(saved)).thenReturn(resp);

        ReviewResponseDto result = reviewService.createReview(userId, orderId, createDto);

        assertNotNull(result);
        assertEquals(resp.getId(), result.getId());
        assertEquals("Nice tour", result.getComment());
        assertEquals(5, result.getRating());

        verify(orderRepository, times(1)).findById(orderId);
        verify(reviewRepository, times(1)).existsByOrderId(orderId);
        verify(reviewMapper, times(1)).toReview(createDto);

        verify(reviewRepository, times(1)).save(reviewCaptor.capture());
        Review toSave = reviewCaptor.getValue();
        assertSame(order, toSave.getOrder(), "Review must be linked to order");

        verify(reviewMapper, times(1)).toReviewResponseDto(saved);
    }

    @Test
    void updateReview_whenReviewNotFound_throwsResourceNotFound() {
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> reviewService.updateReview(userId, reviewId, new ReviewUpdateDto()));

        verify(reviewRepository, times(1)).findById(reviewId);
        verifyNoMoreInteractions(reviewRepository);
        verifyNoInteractions(reviewMapper);
    }

    @Test
    void updateReview_whenNotOwner_throwsUserAccessDenied() {
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();

        User other = new User();
        other.setId(UUID.randomUUID());

        Order order = new Order();
        order.setUser(other);

        Review review = new Review();
        review.setId(reviewId);
        review.setOrder(order);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        assertThrows(UserAccessDeniedException.class,
                () -> reviewService.updateReview(userId, reviewId, new ReviewUpdateDto("x", 4)));

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewMapper, never()).updateFromReviewDto(any(), any());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void updateReview_happyPath_callsMapperUpdate_savesAndReturnsDto() {
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Order order = new Order();
        order.setUser(user);

        Review review = new Review();
        review.setId(reviewId);
        review.setOrder(order);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        ReviewUpdateDto updateDto = new ReviewUpdateDto("Updated", 4);

        Review saved = new Review();
        saved.setId(reviewId);
        saved.setOrder(order);

        when(reviewRepository.save(review)).thenReturn(saved);

        ReviewResponseDto resp = new ReviewResponseDto();
        resp.setId(reviewId);
        resp.setComment("Updated");
        resp.setRating(4);

        when(reviewMapper.toReviewResponseDto(saved)).thenReturn(resp);

        ReviewResponseDto result = reviewService.updateReview(userId, reviewId, updateDto);

        assertNotNull(result);
        assertEquals(reviewId, result.getId());

        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewMapper, times(1)).updateFromReviewDto(updateDto, review);
        verify(reviewRepository, times(1)).save(review);
        verify(reviewMapper, times(1)).toReviewResponseDto(saved);
    }
}

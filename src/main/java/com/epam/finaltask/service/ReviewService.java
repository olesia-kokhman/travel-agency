package com.epam.finaltask.service;

import com.epam.finaltask.dto.review.ReviewCreateDto;
import com.epam.finaltask.dto.review.ReviewResponseDto;
import com.epam.finaltask.dto.review.ReviewUpdateDto;
import com.epam.finaltask.exception.BusinessValidationException;
import com.epam.finaltask.exception.ResourceNotFoundException;
import com.epam.finaltask.exception.UserAccessDeniedException;
import com.epam.finaltask.mapper.ReviewMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.Review;
import com.epam.finaltask.model.enums.OrderStatus;
import com.epam.finaltask.repository.OrderRepository;
import com.epam.finaltask.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final OrderRepository orderRepository;

    // for admin
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllByTour(UUID tourId) {
        return reviewRepository.findAllByOrderTourId(tourId)
                .stream()
                .map(reviewMapper::toReviewResponseDto)
                .toList();
    }

    // for user
    @Transactional
    public ReviewResponseDto createReview(UUID userId, UUID orderId, ReviewCreateDto reviewCreateDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new UserAccessDeniedException();
        }

        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessValidationException("Only paid orders can be reviewed");
        }

        if (reviewRepository.existsByOrderId(orderId)) {
            throw new BusinessValidationException("Review for this order already exists");
        }

        Review review = reviewMapper.toReview(reviewCreateDto);
        review.setOrder(order);

        return reviewMapper.toReviewResponseDto(reviewRepository.save(review));
    }

    // for user
    @Transactional
    public ReviewResponseDto updateReview(UUID userId, UUID reviewId, ReviewUpdateDto reviewUpdateDto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", reviewId));

        if(!review.getOrder().getUser().getId().equals(userId)) {
            throw new UserAccessDeniedException();
        }

        reviewMapper.updateFromReviewDto(reviewUpdateDto, review);
        return reviewMapper.toReviewResponseDto(reviewRepository.save(review));
    }


}

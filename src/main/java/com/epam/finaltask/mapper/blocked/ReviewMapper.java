package com.epam.finaltask.mapper.blocked;

import com.epam.finaltask.dto.order.review.ReviewRequestDto;
import com.epam.finaltask.dto.order.review.ReviewResponseDto;
import com.epam.finaltask.model.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(ReviewRequestDto reviewRequestDto);
    ReviewResponseDto toReviewResponseDto(Review review);
    void updateFromReviewDto(ReviewRequestDto reviewRequestDto, @MappingTarget Review review);
}

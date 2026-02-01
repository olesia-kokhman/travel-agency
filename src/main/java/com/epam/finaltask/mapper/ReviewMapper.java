package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.review.ReviewCreateDto;
import com.epam.finaltask.dto.review.ReviewResponseDto;
import com.epam.finaltask.dto.review.ReviewUpdateDto;
import com.epam.finaltask.model.entity.Review;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toReview(ReviewCreateDto reviewCreateDto);
    ReviewResponseDto toReviewResponseDto(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromReviewDto(ReviewUpdateDto reviewCreateDto, @MappingTarget Review review);
}

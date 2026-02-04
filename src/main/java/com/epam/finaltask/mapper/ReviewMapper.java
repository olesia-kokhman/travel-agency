package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.review.ReviewCreateDto;
import com.epam.finaltask.dto.review.ReviewResponseDto;
import com.epam.finaltask.dto.review.ReviewUpdateDto;
import com.epam.finaltask.model.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    Review toReview(ReviewCreateDto reviewCreateDto);


    @Mapping(target = "orderId", source = "order.id")
    ReviewResponseDto toReviewResponseDto(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromReviewDto(ReviewUpdateDto reviewCreateDto, @MappingTarget Review review);
}

package com.epam.finaltask.mapper.blocked;

import com.epam.finaltask.dto.tour.image.TourImageRequestDto;
import com.epam.finaltask.dto.tour.image.TourImageResponseDto;
import com.epam.finaltask.model.entity.TourImage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TourImageMapper {

    TourImage toTourImage(TourImageRequestDto tourImageRequestDto);
    TourImageResponseDto toTourImageResponseDto(TourImage tourImage);

    void updateTourImageFromDto(TourImageRequestDto tourImageRequestDto, @MappingTarget TourImage tourImage);
}

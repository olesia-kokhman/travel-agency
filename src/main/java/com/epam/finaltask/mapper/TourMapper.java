package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.tour.TourRequestDto;
import com.epam.finaltask.dto.tour.TourResponseDto;
import com.epam.finaltask.model.entity.Tour;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TourMapper {

    Tour toTour(TourRequestDto tourRequestDto);
    TourResponseDto toTourResponseDto(Tour tour);

    void updateTourFromDto(TourRequestDto tourRequestDto, @MappingTarget Tour tour);
}

package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.tour.TourCreateDto;
import com.epam.finaltask.dto.tour.TourResponseDto;
import com.epam.finaltask.dto.tour.TourUpdateDto;
import com.epam.finaltask.model.entity.Tour;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TourMapper {

    Tour toTour(TourCreateDto tourCreateDto);
    TourResponseDto toTourResponseDto(Tour tour);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTourFromDto(TourUpdateDto tourUpdateDto, @MappingTarget Tour tour);
}

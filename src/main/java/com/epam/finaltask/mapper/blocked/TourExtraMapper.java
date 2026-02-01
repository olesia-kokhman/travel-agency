package com.epam.finaltask.mapper.blocked;

import com.epam.finaltask.dto.tour.extra.TourExtraRequestDto;
import com.epam.finaltask.dto.tour.extra.TourExtraResponseDto;
import com.epam.finaltask.model.entity.TourExtra;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TourExtraMapper {
    TourExtra toTourExtra(TourExtraRequestDto tourExtraRequestDto);
    TourExtraResponseDto toTourExtraResponseDto(TourExtra tourExtra);
    void updateTourExtraFromDto(TourExtraRequestDto tourExtraRequestDto, @MappingTarget TourExtra tourExtra);
}

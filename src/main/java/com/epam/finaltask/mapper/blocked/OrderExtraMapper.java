package com.epam.finaltask.mapper.blocked;

import com.epam.finaltask.dto.order.extra.OrderExtraRequestDto;
import com.epam.finaltask.dto.order.extra.OrderExtraResponseDto;
import com.epam.finaltask.model.entity.OrderExtra;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderExtraMapper {
    OrderExtra toOrderExtra(OrderExtraRequestDto orderExtraRequestDto);
    OrderExtraResponseDto toOrderExtraResponseDto(OrderExtra orderExtra);
    void updateFromOrderExtraDto(OrderExtraRequestDto orderExtraRequestDto, @MappingTarget OrderExtra orderExtra);
}

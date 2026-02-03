package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.order.OrderCreateDto;
import com.epam.finaltask.dto.order.OrderResponseDto;
import com.epam.finaltask.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderCreateDto orderCreateDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tourId", source = "tour.id")
    OrderResponseDto toOrderResponseDto(Order order);
}

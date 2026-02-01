package com.epam.finaltask.mapper.blocked;

import com.epam.finaltask.dto.ticket.TicketRequestDto;
import com.epam.finaltask.dto.ticket.TicketResponseDto;
import com.epam.finaltask.model.entity.TicketExtra;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicketExtraMapper {

    TicketExtra toTicketExtra(TicketRequestDto ticketRequestDto);
    TicketResponseDto toTicketResponseDto(TicketExtra ticketExtra);
    void updateFromTicketDto(TicketRequestDto ticketRequestDto, @MappingTarget TicketExtra ticketExtra);
}

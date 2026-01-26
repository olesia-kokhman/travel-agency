package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.ticket.TicketRequestDto;
import com.epam.finaltask.dto.ticket.TicketResponseDto;
import com.epam.finaltask.model.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toTicket(TicketRequestDto ticketRequestDto);
    TicketResponseDto toTicketResponseDto(Ticket ticket);
    void updateFromTicketDto(TicketRequestDto ticketRequestDto, @MappingTarget Ticket ticket);
}

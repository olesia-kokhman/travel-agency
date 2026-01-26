package com.epam.finaltask.dto.ticket;

import com.epam.finaltask.dto.ticket.extra.TicketExtraResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TicketResponseDto {

    private UUID id;

    private String ticketNumber;
    private LocalDateTime issuedAt;

    private UUID orderId;

    private List<TicketExtraResponseDto> items;
}

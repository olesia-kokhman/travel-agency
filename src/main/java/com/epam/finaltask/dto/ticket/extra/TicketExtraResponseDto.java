package com.epam.finaltask.dto.ticket.extra;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TicketExtraResponseDto {

    private UUID id;

    private String itemNumber;
    private LocalDateTime issuedAt;

    private UUID ticketId;
}

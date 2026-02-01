package com.epam.finaltask.controller.blocked;

import com.epam.finaltask.dto.ApiSuccessResponse;
import com.epam.finaltask.dto.ticket.TicketRequestDto;
import com.epam.finaltask.dto.ticket.TicketResponseDto;
import com.epam.finaltask.service.blocked.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{user_id}/orders/{order_id}/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<TicketResponseDto>> readTicketByOrder(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<TicketResponseDto>> createTicket(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody TicketRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping
    public ResponseEntity<ApiSuccessResponse<TicketResponseDto>> updateTicket(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody TicketRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<ApiSuccessResponse<Void>> deleteTicket(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

}

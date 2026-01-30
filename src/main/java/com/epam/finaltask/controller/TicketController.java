package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.ticket.TicketRequestDto;
import com.epam.finaltask.dto.ticket.TicketResponseDto;
import com.epam.finaltask.service.TicketService;
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
    public ResponseEntity<ApiResponse<TicketResponseDto>> readTicketByOrder(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponseDto>> createTicket(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody TicketRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping
    public ResponseEntity<ApiResponse<TicketResponseDto>> updateTicket(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody TicketRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteTicket(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

}

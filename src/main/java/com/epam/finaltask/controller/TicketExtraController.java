package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ticket.extra.TicketExtraRequestDto;
import com.epam.finaltask.dto.ticket.extra.TicketExtraResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.service.TicketExtraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users/{user_id}/orders/{order_id}/ticket/ticket_extras")
@RequiredArgsConstructor
public class TicketExtraController {

    private final TicketExtraService ticketExtraService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketExtraResponseDto>>> readAllItemsByTicket(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId
    ) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketExtraResponseDto>> readItemById(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TicketExtraResponseDto>> createItem(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @Valid @RequestBody TicketExtraRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketExtraResponseDto>> updateItem(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @PathVariable("id") UUID id,
            @Valid @RequestBody TicketExtraRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable("user_id") UUID userId,
            @PathVariable("order_id") UUID orderId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }
}

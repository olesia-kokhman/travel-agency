package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.tour.TourRequestDto;
import com.epam.finaltask.dto.tour.TourResponseDto;
import com.epam.finaltask.service.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourResponseDto>>> readAllTours() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourResponseDto>> readTourById(@PathVariable("id") UUID id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourResponseDto>> createTour(@Valid @RequestBody TourRequestDto requestDto) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourResponseDto>> updateTour(@PathVariable("id") UUID id,
                                                                   @Valid @RequestBody TourRequestDto requestDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTour(@PathVariable("id") UUID id) {
        return null;
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<TourResponseDto>> updateTourActive(@PathVariable("id") UUID id,
                                                                         @RequestParam("active") Boolean active) {
        return null;
    }

    @PatchMapping("/{id}/hot")
    public ResponseEntity<ApiResponse<TourResponseDto>> updateTourHot(@PathVariable("id") UUID id,
                                                                      @RequestParam("hot") Boolean hot) {
        return null;
    }

}

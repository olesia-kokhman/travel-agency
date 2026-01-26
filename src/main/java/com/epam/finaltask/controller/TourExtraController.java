package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.tour.extra.TourExtraRequestDto;
import com.epam.finaltask.dto.tour.extra.TourExtraResponseDto;
import com.epam.finaltask.service.TourExtraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tours/{tour_id}/extras")
@RequiredArgsConstructor
public class TourExtraController {

    private final TourExtraService tourExtraService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourExtraResponseDto>>> readAllExtrasByTour(@PathVariable("tour_id") UUID tourId) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourExtraResponseDto>> readExtraById(@PathVariable("tour_id") UUID tourId,
                                                                           @PathVariable("id") UUID id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourExtraResponseDto>> createExtra(@PathVariable("tour_id") UUID tourId,
                                                                         @Valid @RequestBody TourExtraRequestDto requestDto) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourExtraResponseDto>> updateExtra(@PathVariable("tour_id") UUID tourId,
                                                                         @PathVariable("id")  UUID id,
                                                                         @Valid @RequestBody TourExtraRequestDto requestDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExtra(@PathVariable("tour_id") UUID tourId,
                                                         @PathVariable("id")  UUID id) {
        return null;
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<TourExtraResponseDto>> updateExtraActive(@PathVariable("tour_id") UUID tourId,
                                                                               @PathVariable("id")  UUID id,
                                                                               @RequestParam("active") Boolean active) {
        return null;
    }

}

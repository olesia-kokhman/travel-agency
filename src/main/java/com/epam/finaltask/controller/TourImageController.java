package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiResponse;
import com.epam.finaltask.dto.tour.image.TourImageRequestDto;
import com.epam.finaltask.dto.tour.image.TourImageResponseDto;
import com.epam.finaltask.service.TourImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/tour/{tour_id}/images")
@RequiredArgsConstructor
public class TourImageController {

    private final TourImageService tourImageService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TourImageResponseDto>>> readAllImagesByTour(
            @PathVariable("tour_id") UUID tourId
    ) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TourImageResponseDto>> readImageById(
            @PathVariable("tour_id") UUID tourId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TourImageResponseDto>> createImage(
            @PathVariable("tour_id") UUID tourId,
            @Valid @RequestBody TourImageRequestDto requestDto
    ) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TourImageResponseDto>> updateImage(
            @PathVariable("tour_id") UUID tourId,
            @PathVariable("id") UUID id,
            @Valid @RequestBody TourImageRequestDto requestDto
    ) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @PathVariable("tour_id") UUID tourId,
            @PathVariable("id") UUID id
    ) {
        return null;
    }

    @PatchMapping("/{id}/main")
    public ResponseEntity<ApiResponse<TourImageResponseDto>> updateImageMain(
            @PathVariable("tour_id") UUID tourId,
            @PathVariable("id") UUID id,
            @RequestParam("main") Boolean main
    ) {
        return null;
    }
}

package com.epam.finaltask.controller;

import com.epam.finaltask.dto.apiresponse.ApiPageResponse;
import com.epam.finaltask.dto.apiresponse.ApiSuccessResponse;
import com.epam.finaltask.dto.tour.TourHotUpdateDto;
import com.epam.finaltask.dto.tour.TourCreateDto;
import com.epam.finaltask.dto.tour.TourResponseDto;
import com.epam.finaltask.dto.tour.TourUpdateDto;
import com.epam.finaltask.repository.specifications.filters.TourFilter;
import com.epam.finaltask.service.TourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
@Validated
public class TourController {

    private final TourService tourService;

    @GetMapping
    public ResponseEntity<ApiPageResponse<TourResponseDto>> getAll(
            @Valid @ModelAttribute TourFilter filter,
            Pageable pageable) {

        Page<TourResponseDto> page = tourService.getAll(filter, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiPageResponse.from(page, 200, "Tours fetched successfully."));
    }

    @GetMapping("/{tour_id}")
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> getById(@PathVariable("tour_id") UUID tourId) {
        TourResponseDto dto = tourService.getById(tourId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Tour fetched successfully.", dto));
    }

    @PostMapping // admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> create(@Valid @RequestBody TourCreateDto requestDto) {
        TourResponseDto created = tourService.create(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiSuccessResponse<>(201, "Tour created successfully.", created));
    }

    @PatchMapping("/{tour_id}") // admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> update(@PathVariable("tour_id") UUID tourId,
                                                                      @Valid @RequestBody TourUpdateDto tourUpdateDto) {
        TourResponseDto updated = tourService.update(tourId, tourUpdateDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Tour updated successfully.", updated));
    }

    @DeleteMapping("/{tour_id}") // admin
    public ResponseEntity<ApiSuccessResponse<Void>> deleteTour(@PathVariable("tour_id") UUID tourId) {
        tourService.delete(tourId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tour_id}/hot") // manager and admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> updateHot(@PathVariable("tour_id") UUID tourId,
                                                                         @Valid @RequestBody TourHotUpdateDto tourHotUpdateDto) {
        TourResponseDto updated = tourService.updateHot(tourId, tourHotUpdateDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiSuccessResponse<>(200, "Tour hot status updated successfully.", updated));
    }

}

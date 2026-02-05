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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

        Page<TourResponseDto> tourPage = tourService.getAll(filter, pageable);
        return ResponseEntity.ok(ApiPageResponse.from(tourPage, 200, "success"));
    }

    @GetMapping("/{tour_id}")
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> getById(@PathVariable("tour_id") UUID tourId) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Tour is successfully read",
                tourService.getById(tourId)));
    }

    @PostMapping // admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> create(@Valid @RequestBody TourCreateDto requestDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Tour is successfully created",
                tourService.create(requestDto)));
    }

    @PatchMapping("/{tour_id}") // admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> update(@PathVariable("tour_id") UUID tourId,
                                                                      @Valid @RequestBody TourUpdateDto tourUpdateDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Tour is successfully updated",
                tourService.update(tourId, tourUpdateDto)));
    }

    @DeleteMapping("/{tour_id}") // admin
    public ResponseEntity<ApiSuccessResponse<Void>> deleteTour(@PathVariable("tour_id") UUID tourId) {
        tourService.delete(tourId);
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Tour is successfully deleted", null));
    }


    @PatchMapping("/{tour_id}/hot") // manager and admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> updateHot(@PathVariable("tour_id") UUID tourId,
                                                                         @Valid @RequestBody TourHotUpdateDto tourHotUpdateDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>(200, "Tour is successfully updated to hot",
                tourService.updateHot(tourId, tourHotUpdateDto)));
    }

}

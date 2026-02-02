package com.epam.finaltask.controller;

import com.epam.finaltask.dto.ApiSuccessResponse;
import com.epam.finaltask.dto.tour.TourHotUpdateDto;
import com.epam.finaltask.dto.tour.TourCreateDto;
import com.epam.finaltask.dto.tour.TourResponseDto;
import com.epam.finaltask.dto.tour.TourUpdateDto;
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
    public ResponseEntity<ApiSuccessResponse<List<TourResponseDto>>> getAll() {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Tours are successfully read",
                tourService.getAll()));
    }

    @GetMapping("/{tour_id}")
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> getById(@PathVariable("tour_id") UUID tourId) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Tour is successfully read",
                tourService.getById(tourId)));
    }

    @PostMapping // admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> create(@Valid @RequestBody TourCreateDto requestDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Tour is successfully created",
                tourService.create(requestDto)));
    }

    @PatchMapping("/{tour_id}") // admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> update(@PathVariable("tour_id") UUID tourId,
                                                                      @Valid @RequestBody TourUpdateDto tourUpdateDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Tour is successfully updated",
                tourService.update(tourId, tourUpdateDto)));
    }

    @DeleteMapping("/{tour_id}") // admin
    public ResponseEntity<ApiSuccessResponse<Void>> deleteTour(@PathVariable("tour_id") UUID tourId) {
        tourService.delete(tourId);
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Tour is successfully deleted", null));
    }


    @PatchMapping("/{tour_id}/hot") // manager and admin
    public ResponseEntity<ApiSuccessResponse<TourResponseDto>> updateHot(@PathVariable("tour_id") UUID tourId,
                                                                         @Valid @RequestBody TourHotUpdateDto tourHotUpdateDto) {
        return ResponseEntity.ok(new ApiSuccessResponse<>("OK", "Tour is successfully updated to hot",
                tourService.updateHot(tourId, tourHotUpdateDto)));
    }

}

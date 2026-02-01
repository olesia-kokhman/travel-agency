package com.epam.finaltask.controller.blocked;

import com.epam.finaltask.dto.ApiSuccessResponse;
import com.epam.finaltask.dto.tour.extra.TourExtraRequestDto;
import com.epam.finaltask.dto.tour.extra.TourExtraResponseDto;
import com.epam.finaltask.service.blocked.TourExtraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tours/{tour_id}/extras")
@RequiredArgsConstructor
public class TourExtraController {

    private final TourExtraService tourExtraService;

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<TourExtraResponseDto>>> readAllExtrasByTour(@PathVariable("tour_id") UUID tourId) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<TourExtraResponseDto>> readExtraById(@PathVariable("tour_id") UUID tourId,
                                                                                  @PathVariable("id") UUID id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<TourExtraResponseDto>> createExtra(@PathVariable("tour_id") UUID tourId,
                                                                                @Valid @RequestBody TourExtraRequestDto requestDto) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<TourExtraResponseDto>> updateExtra(@PathVariable("tour_id") UUID tourId,
                                                                                @PathVariable("id")  UUID id,
                                                                                @Valid @RequestBody TourExtraRequestDto requestDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteExtra(@PathVariable("tour_id") UUID tourId,
                                                                @PathVariable("id")  UUID id) {
        return null;
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiSuccessResponse<TourExtraResponseDto>> updateExtraActive(@PathVariable("tour_id") UUID tourId,
                                                                                      @PathVariable("id")  UUID id,
                                                                                      @RequestParam("active") Boolean active) {
        return null;
    }

}

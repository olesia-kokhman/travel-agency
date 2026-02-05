package com.epam.finaltask.service;

import com.epam.finaltask.dto.tour.TourHotUpdateDto;
import com.epam.finaltask.dto.tour.TourCreateDto;
import com.epam.finaltask.dto.tour.TourResponseDto;
import com.epam.finaltask.dto.tour.TourUpdateDto;
import com.epam.finaltask.exception.exceptions.ResourceNotFoundException;
import com.epam.finaltask.mapper.TourMapper;
import com.epam.finaltask.model.entity.Tour;
import com.epam.finaltask.repository.TourRepository;
import com.epam.finaltask.repository.specifications.TourSpecification;
import com.epam.finaltask.repository.specifications.filters.TourFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;

    @Transactional(readOnly = true)
    public Page<TourResponseDto> getAll(TourFilter filter, Pageable pageable) {

        Sort hotFirst = Sort.by(Sort.Order.desc("hot"));
        Sort userOrDefault = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by(Sort.Order.desc("createdAt"));

        Sort finalSort = hotFirst.and(userOrDefault);

        return tourRepository.findAll(TourSpecification.build(filter),
                        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort))
                .map(tourMapper::toTourResponseDto);
    }

    @Transactional(readOnly = true)
    public TourResponseDto getById(UUID tourId) {
        return tourMapper.toTourResponseDto(tourRepository.findById(tourId).orElseThrow(() ->
                new ResourceNotFoundException("Tour", tourId)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TourResponseDto create(TourCreateDto tourCreateDto) {
        Tour tour = tourMapper.toTour(tourCreateDto);
        return tourMapper.toTourResponseDto(tourRepository.save(tour));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TourResponseDto update(UUID tourId, TourUpdateDto tourUpdateDto) {
        Tour currentTour = tourRepository.findById(tourId).orElseThrow(() -> new ResourceNotFoundException("Tour", tourId));

        tourMapper.updateTourFromDto(tourUpdateDto, currentTour);
        return tourMapper.toTourResponseDto(tourRepository.save(currentTour));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(UUID tourId) {
        try {
            tourRepository.deleteById(tourId);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("Tour", tourId);
        }
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Transactional
    public TourResponseDto updateHot(UUID tourId, TourHotUpdateDto tourHotUpdateDto) {
        Tour currentTour = tourRepository.findById(tourId).orElseThrow(() -> new ResourceNotFoundException("Tour", tourId));
        currentTour.setHot(tourHotUpdateDto.isHot());
        return tourMapper.toTourResponseDto(tourRepository.save(currentTour));
    }


}

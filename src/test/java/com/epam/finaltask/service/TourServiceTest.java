package com.epam.finaltask.service;

import com.epam.finaltask.dto.tour.*;
import com.epam.finaltask.exception.exceptions.ResourceNotFoundException;
import com.epam.finaltask.mapper.TourMapper;
import com.epam.finaltask.model.entity.Tour;
import com.epam.finaltask.repository.TourRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourServiceTest {

    @Mock private TourRepository tourRepository;
    @Mock private TourMapper tourMapper;

    @InjectMocks
    private TourService tourService;


    @Test
    void getById_whenNotFound_throwsResourceNotFound() {
        UUID tourId = UUID.randomUUID();
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourService.getById(tourId));

        verify(tourRepository, times(1)).findById(tourId);
        verifyNoInteractions(tourMapper);
    }

    @Test
    void getById_whenFound_returnsMappedDto() {
        UUID tourId = UUID.randomUUID();

        Tour tour = new Tour();

        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));

        TourResponseDto dto = new TourResponseDto();
        dto.setId(tourId);

        when(tourMapper.toTourResponseDto(tour)).thenReturn(dto);

        TourResponseDto result = tourService.getById(tourId);

        assertNotNull(result);
        assertEquals(tourId, result.getId());

        verify(tourRepository, times(1)).findById(tourId);
        verify(tourMapper, times(1)).toTourResponseDto(tour);
    }


    @Test
    void create_mapsSavesAndReturnsDto() {
        TourCreateDto createDto = new TourCreateDto();

        Tour mapped = new Tour();
        when(tourMapper.toTour(createDto)).thenReturn(mapped);

        Tour saved = new Tour();
        UUID savedId = UUID.randomUUID();

        when(tourRepository.save(mapped)).thenReturn(saved);

        TourResponseDto resp = new TourResponseDto();
        resp.setId(savedId);

        when(tourMapper.toTourResponseDto(saved)).thenReturn(resp);

        TourResponseDto result = tourService.create(createDto);

        assertNotNull(result);
        assertEquals(savedId, result.getId());

        verify(tourMapper, times(1)).toTour(createDto);
        verify(tourRepository, times(1)).save(mapped);
        verify(tourMapper, times(1)).toTourResponseDto(saved);
    }

    @Test
    void update_whenNotFound_throwsResourceNotFound() {
        UUID tourId = UUID.randomUUID();
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tourService.update(tourId, new TourUpdateDto()));

        verify(tourRepository, times(1)).findById(tourId);
        verifyNoInteractions(tourMapper);
        verify(tourRepository, never()).save(any());
    }

    @Test
    void update_happyPath_callsMapperUpdate_savesAndReturnsDto() {
        UUID tourId = UUID.randomUUID();

        Tour current = new Tour();
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(current));

        TourUpdateDto updateDto = new TourUpdateDto();

        Tour saved = new Tour();
        when(tourRepository.save(current)).thenReturn(saved);

        TourResponseDto resp = new TourResponseDto();
        resp.setId(tourId);
        when(tourMapper.toTourResponseDto(saved)).thenReturn(resp);

        TourResponseDto result = tourService.update(tourId, updateDto);

        assertNotNull(result);
        assertEquals(tourId, result.getId());

        verify(tourRepository, times(1)).findById(tourId);
        verify(tourMapper, times(1)).updateTourFromDto(updateDto, current);
        verify(tourRepository, times(1)).save(current);
        verify(tourMapper, times(1)).toTourResponseDto(saved);
    }

    @Test
    void delete_happyPath_callsRepositoryDelete() {
        UUID tourId = UUID.randomUUID();
        doNothing().when(tourRepository).deleteById(tourId);

        tourService.delete(tourId);

        verify(tourRepository, times(1)).deleteById(tourId);
    }

    @Test
    void delete_whenEmptyResult_translatesToResourceNotFound() {
        UUID tourId = UUID.randomUUID();
        doThrow(new EmptyResultDataAccessException(1)).when(tourRepository).deleteById(tourId);

        assertThrows(ResourceNotFoundException.class, () -> tourService.delete(tourId));

        verify(tourRepository, times(1)).deleteById(tourId);
    }

    @Test
    void updateHot_whenNotFound_throwsResourceNotFound() {
        UUID tourId = UUID.randomUUID();
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tourService.updateHot(tourId, new TourHotUpdateDto(true)));

        verify(tourRepository, times(1)).findById(tourId);
        verify(tourRepository, never()).save(any());
        verifyNoInteractions(tourMapper);
    }

    @Test
    void updateHot_happyPath_setsHot_savesAndReturnsDto() {
        UUID tourId = UUID.randomUUID();

        Tour current = new Tour();
        current.setHot(false);

        when(tourRepository.findById(tourId)).thenReturn(Optional.of(current));

        Tour saved = new Tour();
        saved.setHot(true);
        when(tourRepository.save(current)).thenReturn(saved);

        TourResponseDto resp = new TourResponseDto();
        resp.setId(tourId);
        resp.setHot(true);

        when(tourMapper.toTourResponseDto(saved)).thenReturn(resp);

        TourResponseDto result = tourService.updateHot(tourId, new TourHotUpdateDto(true));

        assertNotNull(result);
        assertTrue(result.isHot());

        assertTrue(current.isHot());

        verify(tourRepository, times(1)).findById(tourId);
        verify(tourRepository, times(1)).save(current);
        verify(tourMapper, times(1)).toTourResponseDto(saved);
    }
}

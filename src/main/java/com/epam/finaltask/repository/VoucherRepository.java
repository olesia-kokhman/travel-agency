package com.epam.finaltask.repository;

import java.util.List;
import java.util.UUID;

import com.epam.finaltask.model.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Tour, UUID> {
    List<Tour> findAllByUserId(UUID userId);
    List<Tour> findAllByTourType(TourType tourType);
    List<Tour> findAllByTransferType(TransferType transferType);
    List<Tour> findAllByPrice(Double price);
    List<Tour> findAllByHotelType(HotelType hotelType);
}

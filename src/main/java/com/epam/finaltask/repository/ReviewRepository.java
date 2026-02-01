package com.epam.finaltask.repository;

import com.epam.finaltask.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findAllByOrderTourId(UUID tourId);
    boolean existsByOrderId(UUID orderId);

}

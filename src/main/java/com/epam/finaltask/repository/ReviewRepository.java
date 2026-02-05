package com.epam.finaltask.repository;

import com.epam.finaltask.model.entity.Review;
import com.epam.finaltask.model.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findAllByOrderTourId(UUID tourId);
    boolean existsByOrderId(UUID orderId);

    @Query("select r from Review r join r.order o where o.user.id = :userId order by r.createdAt desc")
    List<Review> findAllByUserId(UUID userId);


}

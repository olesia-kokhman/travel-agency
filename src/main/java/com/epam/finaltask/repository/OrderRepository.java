package com.epam.finaltask.repository;

import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    List<Order> findAllByUserId(UUID userId);
    Optional<Order> findByIdAndUserId(UUID orderId, UUID userId);
    boolean existsByOrderNumber(String orderNumber);

}

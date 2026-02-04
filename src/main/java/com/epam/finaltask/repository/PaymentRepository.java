package com.epam.finaltask.repository;

import com.epam.finaltask.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByOrderId(UUID orderId);
    boolean existsByOrderId(UUID orderId);

    @Query(" select p from Payment p join p.order o where o.user.id = :userId order by p.createdAt desc")
    List<Payment> findAllByUserId(UUID userId);

}

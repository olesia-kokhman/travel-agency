package com.epam.finaltask.repository;
import java.util.Optional;
import java.util.UUID;

import com.epam.finaltask.model.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.finaltask.model.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}

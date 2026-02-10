package com.epam.finaltask.repository;

import com.epam.finaltask.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);
    void deleteAllByUserId(UUID userId);

    @Modifying
    @Query("""
        update RefreshToken t
           set t.revoked = true,
               t.revokedAt = :now
         where t.user.id = :userId
           and t.revoked = false
        """)
    int revokeAllActiveByUserId(UUID userId, LocalDateTime now);
}


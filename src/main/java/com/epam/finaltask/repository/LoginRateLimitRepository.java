package com.epam.finaltask.repository;

import com.epam.finaltask.model.entity.LoginRateLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface LoginRateLimitRepository extends JpaRepository<LoginRateLimit, UUID> {

    @Query(value = """
    INSERT INTO login_rate_limits (id, created_at, updated_at, email, window_start, attempts)
    VALUES (
      gen_random_uuid(),
      CAST(:now AS timestamp),
      CAST(:now AS timestamp),
      lower(:email),
      CAST(:now AS timestamp),
      1
    )
    ON CONFLICT (email) DO UPDATE
    SET
      updated_at = CAST(:now AS timestamp),
      window_start = CASE
          WHEN login_rate_limits.window_start < (
              CAST(:now AS timestamp) - (CAST(:windowSeconds AS int) * INTERVAL '1 second')
          )
          THEN CAST(:now AS timestamp)
          ELSE login_rate_limits.window_start
      END,
      attempts = CASE
          WHEN login_rate_limits.window_start < (
              CAST(:now AS timestamp) - (CAST(:windowSeconds AS int) * INTERVAL '1 second')
          )
          THEN 1
          ELSE login_rate_limits.attempts + 1
      END
    RETURNING attempts
    """, nativeQuery = true)
    Integer hitAndReturnAttempts(String email, LocalDateTime now, int windowSeconds);



}

package com.epam.finaltask.auth;

import com.epam.finaltask.exception.exceptions.TooManyLoginAttemptsException;
import com.epam.finaltask.repository.LoginRateLimitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginRateLimitService {

    private static final int LIMIT = 5;
    private static final int WINDOW_SECONDS = 60;

    private final LoginRateLimitRepository loginRateLimitRepository;

    @Transactional
    public void assertAllowed(String rawEmail) {
        String email = normalize(rawEmail);
        if (email == null) {
            log.debug("LOGIN RATE-LIMIT: skip because email is null/blank");
            return;
        }

        Integer attempts = loginRateLimitRepository.hitAndReturnAttempts(
                email, LocalDateTime.now(), WINDOW_SECONDS
        );

        log.debug("LOGIN RATE-LIMIT: email={} attempts={} limit={} windowSec={}",
                email, attempts, LIMIT, WINDOW_SECONDS);

        if (attempts != null && attempts > LIMIT) {
            log.warn("LOGIN RATE-LIMIT BLOCK: email={} attempts={} limit={} windowSec={}",
                    email, attempts, LIMIT, WINDOW_SECONDS);
            throw new TooManyLoginAttemptsException();
        }
    }

    private String normalize(String email) {
        if (email == null) return null;
        email = email.trim().toLowerCase(Locale.ROOT);
        return email.isBlank() ? null : email;
    }
}

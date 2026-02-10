package com.epam.finaltask.service;

import com.epam.finaltask.exception.exceptions.TooManyLoginAttemptsException;
import com.epam.finaltask.repository.LoginRateLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LoginRateLimitService {

    private static final int LIMIT = 5;
    private static final int WINDOW_SECONDS = 60;

    private final LoginRateLimitRepository loginRateLimitRepository;

    @Transactional
    public void assertAllowed(String rawEmail) {
        String email = normalize(rawEmail);
        if (email == null) return;

        Integer attempts = loginRateLimitRepository.hitAndReturnAttempts(email, LocalDateTime.now(), WINDOW_SECONDS);
        if (attempts != null && attempts > LIMIT) {
            throw new TooManyLoginAttemptsException("Too many login attempts. Try again later.");
        }
    }

    private String normalize(String email) {
        if (email == null) return null;
        email = email.trim().toLowerCase(Locale.ROOT);
        return email.isBlank() ? null : email;
    }
}

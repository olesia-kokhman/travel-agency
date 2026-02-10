package com.epam.finaltask.service;

import com.epam.finaltask.model.entity.RefreshToken;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.repository.RefreshTokenRepository;
import com.epam.finaltask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String hash(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash refresh token", e);
        }
    }

    public void save(UUID userId, String refreshToken, LocalDateTime expiresAt) {
        RefreshToken rt = new RefreshToken();
        rt.setUser(userRepository.getReferenceById(userId));
        rt.setTokenHash(hash(refreshToken));
        rt.setExpiresAt(expiresAt);
        rt.setRevoked(false);
        rt.setRevokedAt(null);
        refreshTokenRepository.save(rt);
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findByTokenHash(hash(refreshToken));
    }

    public boolean isActive(String refreshToken) {
        return findByToken(refreshToken)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public void revoke(String refreshToken) {
        findByToken(refreshToken).ifPresent(t -> {
            if (!t.isRevoked()) {
                t.setRevoked(true);
                t.setRevokedAt(LocalDateTime.now());
                refreshTokenRepository.save(t);
            }
        });
    }
}

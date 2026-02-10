package com.epam.finaltask.auth;

import com.epam.finaltask.model.entity.RefreshToken;
import com.epam.finaltask.repository.RefreshTokenRepository;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.util.TokenHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenHasher tokenHasher;

    public void save(UUID userId, String refreshToken, LocalDateTime expiresAt) {
        RefreshToken rt = new RefreshToken();
        rt.setUser(userRepository.getReferenceById(userId));
        rt.setTokenHash(tokenHasher.hashRefreshToken(refreshToken));
        rt.setExpiresAt(expiresAt);
        rt.setRevoked(false);
        rt.setRevokedAt(null);
        refreshTokenRepository.save(rt);
    }

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findByTokenHash(tokenHasher.hashRefreshToken(refreshToken));
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

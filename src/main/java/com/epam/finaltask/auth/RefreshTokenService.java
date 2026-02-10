package com.epam.finaltask.auth;

import com.epam.finaltask.model.entity.RefreshToken;
import com.epam.finaltask.repository.RefreshTokenRepository;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.util.TokenHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenHasher tokenHasher;

    public void save(UUID userId, String refreshToken, LocalDateTime expiresAt) {
        String hash = tokenHasher.hashRefreshToken(refreshToken);

        RefreshToken rt = new RefreshToken();
        rt.setUser(userRepository.getReferenceById(userId));
        rt.setTokenHash(hash);
        rt.setExpiresAt(expiresAt);
        rt.setRevoked(false);
        rt.setRevokedAt(null);

        refreshTokenRepository.save(rt);

        log.info("REFRESH_TOKEN SAVE: userId={} expiresAt={}", userId, expiresAt);
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
        String hash = tokenHasher.hashRefreshToken(refreshToken);

        findByToken(refreshToken).ifPresentOrElse(t -> {
            if (!t.isRevoked()) {
                t.setRevoked(true);
                t.setRevokedAt(LocalDateTime.now());
                refreshTokenRepository.save(t);

                log.info("REFRESH_TOKEN REVOKE: userId={} revokedAt={}",
                        t.getUser().getId(), t.getRevokedAt());
                log.debug("REFRESH_TOKEN REVOKE: tokenHashPrefix={}", prefix(hash));
            } else {
                log.debug("REFRESH_TOKEN REVOKE: alreadyRevoked userId={} revokedAt={}",
                        t.getUser().getId(), t.getRevokedAt());
            }
        }, () -> {
            log.debug("REFRESH_TOKEN REVOKE: tokenNotFound tokenHashPrefix={}", prefix(hash));
        });
    }

    private String prefix(String s) {
        if (s == null) return "null";
        return s.length() <= 8 ? s : s.substring(0, 8);
    }
}

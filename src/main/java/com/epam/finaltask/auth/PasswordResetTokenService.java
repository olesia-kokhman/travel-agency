package com.epam.finaltask.auth;

import com.epam.finaltask.model.entity.PasswordResetToken;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.repository.PasswordResetTokenRepository;
import com.epam.finaltask.repository.RefreshTokenRepository;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.util.TokenGenerator;
import com.epam.finaltask.util.TokenHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetTokenService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final TokenHasher tokenHasher;
    private final MailService mailService;

    @Value("${app.password-reset.ttl-minutes:30}")
    private long ttlMinutes;

    @Value("${app.frontend.reset-password-url-template}")
    private String resetUrlTemplate;

    @Transactional
    public void request(String email) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.info("PWD_RESET REQUEST: emailNotFound email={}", email);
            return;
        }

        var user = userOpt.get();

        log.debug("PWD_RESET REQUEST: clearing existing tokens userId={} email={}",
                user.getId(), user.getEmail());
        resetTokenRepository.deleteAllByUserId(user.getId());

        String rawToken = tokenGenerator.generateUrlSafeToken(32);
        String hash = tokenHasher.hashPasswordResetToken(rawToken);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setTokenHash(hash);
        prt.setExpiresAt(LocalDateTime.now().plusMinutes(ttlMinutes));
        resetTokenRepository.save(prt);

        log.info("PWD_RESET REQUEST: issued userId={} email={} expiresAt={}",
                user.getId(), user.getEmail(), prt.getExpiresAt());

        String url = String.format(resetUrlTemplate, rawToken);
        mailService.sendPasswordResetEmail(user.getEmail(), url);
    }

    @Transactional
    public void confirm(String token, String newPassword) {
        String hash = tokenHasher.hashPasswordResetToken(token);

        PasswordResetToken t = resetTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> {
                    log.warn("PWD_RESET CONFIRM FAIL: invalidToken");
                    return new BadCredentialsException("Invalid token");
                });

        if (t.getUsedAt() != null) {
            log.warn("PWD_RESET CONFIRM FAIL: alreadyUsed userId={} email={} usedAt={}",
                    t.getUser().getId(), t.getUser().getEmail(), t.getUsedAt());
            throw new BadCredentialsException("Token already used");
        }
        if (t.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("PWD_RESET CONFIRM FAIL: expired userId={} email={} expiresAt={}",
                    t.getUser().getId(), t.getUser().getEmail(), t.getExpiresAt());
            throw new BadCredentialsException("Token expired");
        }

        User user = t.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        t.setUsedAt(LocalDateTime.now());
        resetTokenRepository.save(t);

        refreshTokenRepository.revokeAllActiveByUserId(user.getId(), LocalDateTime.now());

        log.info("PWD_RESET CONFIRM OK: userId={} email={} refreshTokensRevoked=true",
                user.getId(), user.getEmail());
    }

    @Transactional
    public void cleanupExpiredOrUsed() {
        resetTokenRepository.deleteExpiredOrUsed(LocalDateTime.now());
        log.debug("PWD_RESET CLEANUP: expired/used tokens deleted");
    }
}

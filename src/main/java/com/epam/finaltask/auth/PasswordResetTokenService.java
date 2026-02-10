package com.epam.finaltask.auth;

import com.epam.finaltask.model.entity.PasswordResetToken;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.repository.PasswordResetTokenRepository;
import com.epam.finaltask.repository.RefreshTokenRepository;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.util.TokenGenerator;
import com.epam.finaltask.util.TokenHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
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
            return;
        }

        var user = userOpt.get();

        resetTokenRepository.deleteAllByUserId(user.getId());

        String rawToken = tokenGenerator.generateUrlSafeToken(32);
        String hash = tokenHasher.hashPasswordResetToken(rawToken);

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setTokenHash(hash);
        prt.setExpiresAt(LocalDateTime.now().plusMinutes(ttlMinutes));
        resetTokenRepository.save(prt);

        String url = String.format(resetUrlTemplate, rawToken);
        mailService.sendPasswordResetEmail(user.getEmail(), url);
    }

    @Transactional
    public void confirm(String token, String newPassword) {
        String hash = tokenHasher.hashPasswordResetToken(token);

        PasswordResetToken t = resetTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));

        if (t.getUsedAt() != null) {
            throw new BadCredentialsException("Token already used");
        }
        if (t.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Token expired");
        }

        User user = t.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        t.setUsedAt(LocalDateTime.now());
        resetTokenRepository.save(t);
        refreshTokenRepository.revokeAllActiveByUserId(user.getId(), LocalDateTime.now());
    }

    @Transactional
    public void cleanupExpiredOrUsed() {
        resetTokenRepository.deleteExpiredOrUsed(LocalDateTime.now());
    }
}

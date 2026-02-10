package com.epam.finaltask.auth;

import com.epam.finaltask.auth.dto.*;
import com.epam.finaltask.exception.exceptions.EmailAlreadyExistsException;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.security.UserDetailsServiceImpl;
import com.epam.finaltask.security.jwt.JwtService;
import com.epam.finaltask.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    private final UserDetailsServiceImpl userDetailsService;
    private final LoginRateLimitService loginRateLimitService;

    public JwtResponseDto login(LoginRequestDto requestDto) {
        try {
            loginRateLimitService.assertAllowed(requestDto.getEmail());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String access = jwtService.generateAccessToken(userDetails);
            String refresh = jwtService.generateRefreshToken(userDetails);

            var expInstant = jwtService.extractExpiration(refresh).toInstant();
            var expiresAt = LocalDateTime.ofInstant(expInstant, java.time.ZoneOffset.UTC);
            refreshTokenService.save(userDetails.getId(), refresh, expiresAt);

            log.info("SECURITY loginSuccess userId={} email={}",
                    userDetails.getId(), userDetails.getUsername());

            JwtResponseDto dto = new JwtResponseDto();
            dto.setJwtAccessToken(access);
            dto.setJwtRefreshToken(refresh);
            return dto;

        } catch (BadCredentialsException ex) {
            log.warn("SECURITY loginFailed email={} reason=bad_credentials", requestDto.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        } catch (DisabledException ex) {
            log.warn("SECURITY loginFailed email={} reason=user_is_blocked", requestDto.getEmail());
            throw new DisabledException("User is blocked");
        } catch (ClassCastException ex) {
            log.error("SECURITY loginFailed email={} reason=unexpected_principal_type",
                    requestDto.getEmail(), ex);
            throw new ClassCastException("Unexpected principal type");
        }
    }

    public void register(RegisterRequestDto requestDto) {
        if (userService.existsByEmail(requestDto.getEmail())) {
            log.warn("SECURITY registerFailed email={} reason=email_exists", requestDto.getEmail());
            throw new EmailAlreadyExistsException(requestDto.getEmail());
        }

        userService.register(requestDto);
        log.info("SECURITY registerSuccess email={}", requestDto.getEmail());
    }

    public JwtResponseDto refresh(RefreshRequestDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            log.warn("SECURITY refreshFailed reason=not_refresh_token");
            throw new BadCredentialsException("Invalid refresh token");
        }

        final String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (JwtException ex) {
            log.warn("SECURITY refreshFailed reason=jwt_invalid exType={}", ex.getClass().getSimpleName());
            throw new BadCredentialsException("Invalid refresh token");
        }

        if (!refreshTokenService.isActive(refreshToken)) {
            log.warn("SECURITY refreshFailed email={} reason=revoked_or_missing", username);
            throw new BadCredentialsException("Invalid refresh token");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        if (!userDetails.isEnabled()) {
            log.warn("SECURITY refreshFailed email={} reason=user_blocked", username);
            throw new DisabledException("User is blocked");
        }

        refreshTokenService.revoke(refreshToken);

        String newAccess = jwtService.generateAccessToken(userDetails);
        String newRefresh = jwtService.generateRefreshToken(userDetails);

        var expInstant = jwtService.extractExpiration(newRefresh).toInstant();
        var expiresAt = LocalDateTime.ofInstant(expInstant, java.time.ZoneOffset.UTC);
        refreshTokenService.save(userDetails.getId(), newRefresh, expiresAt);

        log.info("SECURITY refreshSuccess userId={} email={}", userDetails.getId(), userDetails.getUsername());

        JwtResponseDto dto = new JwtResponseDto();
        dto.setJwtAccessToken(newAccess);
        dto.setJwtRefreshToken(newRefresh);
        return dto;
    }

    public void logout(RefreshRequestDto requestDto) {
        refreshTokenService.revoke(requestDto.getRefreshToken());
        log.info("SECURITY logout refreshRevoked=true");
    }
}

package com.epam.finaltask.auth;

import com.epam.finaltask.auth.dto.*;
import com.epam.finaltask.exception.exceptions.EmailAlreadyExistsException;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.security.jwt.JwtService;
import com.epam.finaltask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public JwtResponseDto login(LoginRequestDto requestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            log.info("SECURITY loginSuccess userId={} email={}",
                    userDetails.getId(), userDetails.getUsername());

            JwtResponseDto dto = new JwtResponseDto();
            dto.setJwtAccessToken(jwtService.generateAccessToken(userDetails));
            dto.setJwtRefreshToken(jwtService.generateRefreshToken(userDetails));
            return dto;

        } catch (BadCredentialsException ex) {
            log.warn("SECURITY loginFailed email={} reason=bad_credentials", requestDto.getEmail());
            throw new BadCredentialsException("Invalid email or password");
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
        return null;
    }

    public void logout(LogoutRequestDto requestDto) {

    }

}

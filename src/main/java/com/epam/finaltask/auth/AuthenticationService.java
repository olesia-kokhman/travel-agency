package com.epam.finaltask.auth;

import com.epam.finaltask.auth.dto.*;
import com.epam.finaltask.dto.user.UserRequestDto;
import com.epam.finaltask.security.UserDetailsImpl;
import com.epam.finaltask.security.jwt.JwtService;
import com.epam.finaltask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JwtResponseDto login(LoginRequestDto requestDto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            JwtResponseDto dto = new JwtResponseDto();

            dto.setJwtAccessToken(jwtService.generateAccessToken(userDetails));
            dto.setJwtRefreshToken(jwtService.generateRefreshToken(userDetails));

            return dto;
        } catch (BadCredentialsException badCredentialsException) {
            throw new BadCredentialsException("Invalid email or password");
        } catch (ClassCastException classCastException) {
            throw new ClassCastException("Unexpected principal type ");
        }

    }

    public JwtResponseDto register(RegisterRequestDto requestDto) {
        UserRequestDto userRequestDto = new UserRequestDto(
                requestDto.getName(),
                requestDto.getSurname(),
                requestDto.getEmail(),
                requestDto.getPhoneNumber(),
                requestDto.getPassword());

        userService.create(userRequestDto);

        return login(new LoginRequestDto(requestDto.getEmail(), requestDto.getPassword()));
    }

    public JwtResponseDto refresh(RefreshRequestDto requestDto) {
        return null;
    }

    public void logout(LogoutRequestDto requestDto) {

    }

}

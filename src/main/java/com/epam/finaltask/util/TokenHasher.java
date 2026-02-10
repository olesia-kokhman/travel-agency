package com.epam.finaltask.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
public class TokenHasher {

    public String sha256Hex(String token, String errorLabel) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash " + errorLabel, e);
        }
    }

    public String hashRefreshToken(String token) {
        return sha256Hex(token, "refresh token");
    }

    public String hashPasswordResetToken(String token) {
        return sha256Hex(token, "password reset token");
    }
}

package com.epam.finaltask.security.jwt;

import com.epam.finaltask.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-lifetime}")
    private long accessLifetime;

    @Value("${app.jwt.refresh-lifetime}")
    private long refreshLifetime;

    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_TOKEN_TYPE = "tokenType";

    public static final String TOKEN_TYPE_ACCESS = "ACCESS";
    public static final String TOKEN_TYPE_REFRESH = "REFRESH";

    public String generateAccessToken(UserDetailsImpl userDetails) {

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessLifetime))
                .claim(CLAIM_ROLES, roles)
                .claim(CLAIM_USER_ID, userDetails.getId().toString())
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .signWith(getSignInKey())
                .compact();
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshLifetime))
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
                .signWith(getSignInKey())
                .compact();
    }

    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public boolean validateToken(String token, UserDetailsImpl userDetails) {
        try {
            Claims claims = parseClaims(token);

            String username = claims.getSubject();
            Date exp = claims.getExpiration();

            return username != null
                    && username.equals(userDetails.getUsername())
                    && exp != null
                    && exp.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims parseClaims(String token) {
        // Throws JwtException if invalid/expired.
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractTokenType(String token) {
        Object v = parseClaims(token).get(CLAIM_TOKEN_TYPE);
        return v == null ? null : v.toString();
    }

    public String extractUserId(String token) {
        Object v = parseClaims(token).get(CLAIM_USER_ID);
        return v == null ? null : v.toString();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object v = parseClaims(token).get(CLAIM_ROLES);
        return v == null ? List.of() : (List<String>) v;
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        return extractRoles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public boolean isAccessToken(String token) {
        return TOKEN_TYPE_ACCESS.equals(extractTokenType(token));
    }

    public boolean isRefreshToken(String token) {
        return TOKEN_TYPE_REFRESH.equals(extractTokenType(token));
    }


}

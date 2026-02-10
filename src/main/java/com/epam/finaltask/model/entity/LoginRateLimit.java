package com.epam.finaltask.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_rate_limits",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_login_rate_limits_email", columnNames = "email")},
        indexes = {
                @Index(name = "idx_login_rate_limits_email", columnList = "email"),
                @Index(name = "idx_login_rate_limits_updated_at", columnList = "updated_at")})
@Getter
@Setter
public class LoginRateLimit extends AuditableEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "window_start", nullable = false)
    private LocalDateTime windowStart;

    @Column(name = "attempts", nullable = false)
    private int attempts;
}

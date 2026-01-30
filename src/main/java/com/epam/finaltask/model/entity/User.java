package com.epam.finaltask.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.epam.finaltask.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
@Getter
@Setter
@AllArgsConstructor()
@NoArgsConstructor
public class User extends AuditableEntity {

	@Column(name = "name", nullable = false, length = 100)
    private String name;

	@Column(name = "surname", nullable = false, length = 100)
	private String surname;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "phone_number", nullable = false, length = 25)
	private String phoneNumber;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "is_active", nullable = false)
	private boolean active;

	@Column(name = "balance", nullable = false, precision = 19, scale = 2)
	private BigDecimal balance = BigDecimal.ZERO;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(name = "role", nullable = false, columnDefinition = "user_role")
    private UserRole role;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();
    
}
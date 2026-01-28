package com.epam.finaltask.model.enums;

public enum UserRole {
    ADMIN,
    MANAGER,
    USER;

    public String asAuthority() {
        return "ROLE_" + name();
    }
}

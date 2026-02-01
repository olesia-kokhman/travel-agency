package com.epam.finaltask.exception;

import java.util.UUID;

public class UserNotFoundException extends GeneralApiException {

    public UserNotFoundException(UUID userId) {
        super("User with id " + userId + " is not found", "USER_NOT_FOUND");
    }
}

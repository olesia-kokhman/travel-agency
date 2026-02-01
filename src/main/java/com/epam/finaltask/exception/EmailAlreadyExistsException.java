package com.epam.finaltask.exception;

public class EmailAlreadyExistsException extends GeneralApiException {

    public EmailAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists", "EMAIL_ALREADY_EXISTS");
    }
}

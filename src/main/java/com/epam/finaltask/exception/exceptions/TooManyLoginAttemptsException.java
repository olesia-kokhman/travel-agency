package com.epam.finaltask.exception.exceptions;

public class TooManyLoginAttemptsException extends GeneralApiException {

    public TooManyLoginAttemptsException() {
        super("Provided user has no access to the resource", "TOO_MANY_REQUESTS");
    }

}

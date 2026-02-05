package com.epam.finaltask.exception.exceptions;

public abstract class GeneralApiException extends RuntimeException {
    private final String errorCode;

    protected GeneralApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}


package com.epam.finaltask.exception.exceptions;

public class BusinessValidationException extends GeneralApiException {

    public BusinessValidationException(String message) {
        super(message, "BUSINESS_VALIDATION_ERROR");
    }
}

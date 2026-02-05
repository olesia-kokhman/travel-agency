package com.epam.finaltask.exception.exceptions;

public class UserAccessDeniedException extends GeneralApiException {

    public UserAccessDeniedException() {
        super("Provided user has no access to the resource", "ACCESS_DENIED");
    }
}

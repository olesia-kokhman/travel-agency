package com.epam.finaltask.exception.exceptions;

import java.util.UUID;

public class ResourceNotFoundException extends GeneralApiException {

    public ResourceNotFoundException(String resourceName, UUID resourceId) {
        super(resourceName + " with id " + resourceId + " is not found", resourceName.toUpperCase() + "_NOT_FOUND");
    }

}

